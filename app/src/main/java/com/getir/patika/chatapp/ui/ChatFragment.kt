package com.getir.patika.chatapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.getir.patika.chatapp.databinding.FragmentChatBinding
import com.getir.patika.chatapp.fake.FakeMessages
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    val viewModel: ChatViewModel by viewModels()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding =
        FragmentChatBinding.inflate(inflater, container, false)

    override fun initializeViews() {
        val adapter = MessageAdapter()
        adapter.saveData(FakeMessages.take(5))
        binding.rvMessages.adapter = adapter
        lifecycleScope.launch {
            delay(2000L)
            adapter.showLoadingIndicator()
            delay(3000L)
            adapter.hideLoadingIndicator()
            adapter.saveData(FakeMessages.take(7))
        }
    }
}
