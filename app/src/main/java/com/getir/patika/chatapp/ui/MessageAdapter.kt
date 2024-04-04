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

class MessageAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(ItemDiff) {

    private val asyncListDiffer = AsyncListDiffer(this, ItemDiff)
    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun getItemViewType(position: Int): Int {
        val message = asyncListDiffer.currentList[position]
        if (!message.isLoaded) {
            return VIEW_TYPE_LOADING
        }
        return if (message.role == Role.MODEL) {
            VIEW_TYPE_MODEL
        } else {
            VIEW_TYPE_USER
        }
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

    /**
     * Updates the data set with the provided list of messages.
     */
    fun saveData(messages: List<Message>) {
        asyncListDiffer.submitList(messages)
    }

    /**
     * Interface for binding chat view holders.
     */
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

    inner class LoadingViewHolder(binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = asyncListDiffer.currentList[position]
        if (message.isLoaded) {
            (holder as? BindableChatViewHolder)?.bind(message)
        }
    }

    private var recyclerView: RecyclerView? = null
    fun attachToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        asyncListDiffer.addListListener(listUpdateCallback)
    }

    fun detachFromRecyclerView() {
        asyncListDiffer.removeListListener(listUpdateCallback)
        this.recyclerView = null
    }

    private val listUpdateCallback =
        AsyncListDiffer.ListListener<Message> { previousList, currentList ->
            if (currentList.size >= previousList.size) {
                recyclerView?.post {
                    recyclerView?.scrollToPosition(currentList.size - 1)
                }
            }
        }

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_MODEL = 2
        private const val VIEW_TYPE_LOADING = 3

        /**
         * DiffUtil.ItemCallback implementation for comparing chat items.
         */
        val ItemDiff = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}
