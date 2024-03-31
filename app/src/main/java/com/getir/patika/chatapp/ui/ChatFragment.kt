package com.getir.patika.chatapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.getir.patika.chatapp.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    val viewModel: ChatViewModel by viewModels()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding =
        FragmentChatBinding.inflate(inflater, container, false)

    override fun initializeViews() {
    }
}
