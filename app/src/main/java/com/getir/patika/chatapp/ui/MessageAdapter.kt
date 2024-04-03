package com.getir.patika.chatapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.getir.patika.chatapp.data.model.Message
import com.getir.patika.chatapp.data.model.Role
import com.getir.patika.chatapp.databinding.ItemLoadingBinding
import com.getir.patika.chatapp.databinding.ItemMessageModelBinding
import com.getir.patika.chatapp.databinding.ItemMessageUserBinding

sealed class ChatItem {
    data class ChatMessage(val message: Message) : ChatItem()
    data object Loading : ChatItem()
}

class MessageAdapter : ListAdapter<ChatItem, RecyclerView.ViewHolder>(ItemDiff) {

    private val asyncListDiffer = AsyncListDiffer(this, ItemDiff)
    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun getItemViewType(position: Int): Int =
        when (val chatItem = asyncListDiffer.currentList[position]) {
            is ChatItem.ChatMessage -> {
                if (chatItem.message.role == Role.USER) {
                    VIEW_TYPE_USER
                } else {
                    VIEW_TYPE_MODEL
                }
            }

            ChatItem.Loading -> VIEW_TYPE_LOADING
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding = ItemMessageUserBinding.inflate(inflater, parent, false)
                UserViewHolder(binding)
            }

            VIEW_TYPE_MODEL -> {
                val binding = ItemMessageModelBinding.inflate(inflater, parent, false)
                ModelViewHolder(binding)
            }

            VIEW_TYPE_LOADING -> {
                val binding = ItemLoadingBinding.inflate(inflater, parent, false)
                LoadingViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun saveData(messages: List<Message>) {
        val chatMessages = messages.map { ChatItem.ChatMessage(it) }
        asyncListDiffer.submitList(chatMessages)
    }

    fun showLoadingIndicator() {
        asyncListDiffer.currentList.toMutableList().apply {
            add(ChatItem.Loading)
        }.let {
            asyncListDiffer.submitList(it)
        }
    }

    fun hideLoadingIndicator() {
        asyncListDiffer.currentList.toMutableList().apply {
            remove(ChatItem.Loading)
        }.let {
            asyncListDiffer.submitList(it)
        }
    }

    interface BindableChatViewHolder {
        fun bind(message: Message)
    }

    inner class UserViewHolder(private val binding: ItemMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root), BindableChatViewHolder {
        override fun bind(message: Message) {
            binding.tvBubbleUser.text = message.message
        }
    }

    inner class ModelViewHolder(private val binding: ItemMessageModelBinding) :
        RecyclerView.ViewHolder(binding.root), BindableChatViewHolder {
        override fun bind(message: Message) {
            binding.tvModel.text = message.message
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatItem = asyncListDiffer.currentList[position]
        if (chatItem is ChatItem.ChatMessage) {
            (holder as? BindableChatViewHolder)?.bind(chatItem.message)
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_MODEL = 2
        private const val VIEW_TYPE_LOADING = 3

        val ItemDiff = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                if (oldItem is ChatItem.ChatMessage && newItem is ChatItem.ChatMessage) {
                    return oldItem.message.id == newItem.message.id
                }
                return false
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                if (oldItem is ChatItem.ChatMessage && newItem is ChatItem.ChatMessage) {
                    return oldItem.message == newItem.message
                }
                return false
            }
        }
    }
}
