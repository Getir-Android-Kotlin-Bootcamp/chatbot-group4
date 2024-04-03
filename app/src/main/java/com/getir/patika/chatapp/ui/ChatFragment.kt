package com.getir.patika.chatapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.getir.patika.chatapp.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private val viewModel: ChatViewModel by viewModels()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding =
        FragmentChatBinding.inflate(inflater, container, false)

    override fun initializeViews() {
        val adapter = MessageAdapter()
        binding.rvMessages.adapter = adapter

        scopeWithLifecycle {
            viewModel.messages.collectLatest { messages ->
                adapter.saveData(messages)
            }
        }

        scopeWithLifecycle {
            viewModel.uiState.map { it.isLoading }.distinctUntilChanged().collectLatest {
                println("Fragment Loading state: $it")
                if (it) {
                    adapter.showLoadingIndicator()
                } else {
                    adapter.hideLoadingIndicator()
                }
            }
        }

        binding.editTextMessage.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                viewModel.onQueryChanged(text.toString())
            }
        }

        binding.sendButton.setOnClickListener {
            viewModel.onSend()
        }

    }

    private fun scopeWithLifecycle(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block = block)
        }
    }
}
