package com.getir.patika.chatapp

import android.view.LayoutInflater
import android.view.ViewGroup
import com.getir.patika.chatapp.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding =
        FragmentChatBinding.inflate(inflater, container, false)

    override fun initializeViews() {
    }
}
