package com.getir.patika.chatapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.getir.patika.chatapp.data.model.Message
import com.getir.patika.chatapp.data.model.Role
import com.getir.patika.chatapp.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    lateinit var messageList: ArrayList<Message>
    val viewModel: ChatViewModel by viewModels()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding =
        FragmentChatBinding.inflate(inflater, container, false)

    override fun initializeViews() {
        createFakeList()
        val adapter = MessageAdapter(messageList)
        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMessages.adapter = adapter
        adapter.notifyDataSetChanged() // List Adapter kullanılınca değişecek

    }

    // To give Fake Data
    fun createFakeList() {
        val message1 = Message(message = "Hi")
        val message2 = Message(message = "Merhaba dawdwadwadwadawdwa")
        val message3 = Message(message = "Sa")
        val message4 = Message(message = "Emmanul Mancy", role = Role.USER) // TODO: role user'dan sonraki mesajlar gösterilmiyor.
        val message5 = Message(message = "Hi")
        val message6 = Message(message = "Hi")
        val message7 = Message(message = "Hi")

        messageList = arrayListOf(message1,message2,message3,message4,message5,message6,message7)
    }
}
