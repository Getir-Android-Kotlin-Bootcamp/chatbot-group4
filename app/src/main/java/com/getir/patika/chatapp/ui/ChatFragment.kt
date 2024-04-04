package com.getir.patika.chatapp.ui

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.getir.patika.chatapp.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Fragment for displaying and managing chat messages.
 */
@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private val viewModel: ChatViewModel by viewModels()
    private val adapter = MessageAdapter()

    /**
     * Inflates the layout and initializes the view binding.
     */
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding =
        FragmentChatBinding.inflate(inflater, container, false)

    /**
     * Initializes the views and sets up event listeners.
     */
    override fun initializeViews() {

        val contentView = requireActivity().findViewById<View>(android.R.id.content)

        scopeWithLifecycle {
            viewModel.observeMessages()
            viewModel.getFirstMessageState()
        }

        with(binding) {
            setupAdapter()
            editTextMessage.observeChanges()

            scopeWithLifecycle {
                contentView.keyboardVisibilityFlow().collectLatest { (isKeyboardVisible, height) ->
                    btnAttach.visibility = if (isKeyboardVisible) View.GONE else View.VISIBLE
                    handleBottomBarMagin(height, isKeyboardVisible)
                }
            }

            sendButton.setOnClickListener {
                beforeSendActions()
                viewModel.onSend(editTextMessage.text.toString())
                editTextMessage.text.clear()
            }
        }
    }

    /**
     * Sets up the adapter for displaying messages.
     */
    private fun FragmentChatBinding.setupAdapter() {
        rvMessages.adapter = adapter
        adapter.attachToRecyclerView(rvMessages)

        scopeWithLifecycle {
            viewModel.messageState.collectLatest {
                adapter.saveData(it)
            }
        }
    }

    /**
     * Actions to be performed before sending a message.
     */
    private fun FragmentChatBinding.beforeSendActions() {
        val focus = editTextMessage.findFocus()
        focus?.let { view ->
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }

    /**
     * Observes changes in the text input field.
     */
    private fun EditText.observeChanges() {
        doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                viewModel.onQueryChanged(text.toString())
            }
        }
        scopeWithLifecycle {
            viewModel.uiState.map { it.query to it.isTextFieldEnabled }.distinctUntilChanged()
                .collectLatest { (query, isTextFieldEnabled) ->
                    if (query != text.toString()) {
                        setText(query)
                    }
                    isEnabled = isTextFieldEnabled
                    isClickable = isTextFieldEnabled
                }
        }
    }

    /**
     * Provides a flow representing keyboard visibility.
     */
    private fun View.keyboardVisibilityFlow(): Flow<Pair<Boolean, Int>> = callbackFlow {
        val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom
            val isKeyboardVisible = keypadHeight > screenHeight * 0.15
            trySend(isKeyboardVisible to keypadHeight)
        }

        viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

        awaitClose {
            viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        }
    }

    /**
     * Adjusts bottom margin of the chat box based on keyboard visibility.
     */
    private fun FragmentChatBinding.handleBottomBarMagin(
        keypadHeight: Int,
        isVisible: Boolean
    ) {
        val params = llChatBox.layoutParams as ConstraintLayout.LayoutParams
        params.bottomMargin = if (isVisible) keypadHeight else 0
        llChatBox.requestLayout()
    }

    /**
     * Executes a coroutine block scoped to the fragment's lifecycle.
     */
    private fun scopeWithLifecycle(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block = block)
        }
    }

    /**
     * Cleans up resources when the fragment is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        adapter.detachFromRecyclerView()
    }
}
