package com.getir.patika.chatapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.getir.patika.chatapp.data.model.Message
import com.getir.patika.chatapp.data.model.Role

import com.getir.patika.chatapp.databinding.ItemMessageModelBinding
import com.getir.patika.chatapp.databinding.ItemMessageUserBinding
import com.getir.patika.chatapp.util.ROLE_MODEL
import com.getir.patika.chatapp.util.ROLE_USER

class MessageAdapter(val messages: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class UserViewHolder(val binding: ItemMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvBubbleUser.text = message.message

        }

    }

    inner class ModelViewHolder(val binding: ItemMessageModelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvBubbleModel.text = message.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ROLE_USER -> {
                val binding = ItemMessageUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return UserViewHolder(binding)
            }

            ROLE_MODEL -> {
                val binding = ItemMessageModelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ModelViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown view type: $viewType")

        }


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is UserViewHolder -> holder.bind(message)
            is ModelViewHolder -> holder.bind(message)
            else -> throw IllegalArgumentException("Unknown view holder type: ${holder.javaClass.simpleName}")
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (messages[position].role == Role.USER) ROLE_USER else ROLE_MODEL
    }

    override fun getItemCount(): Int {
        return messages.size
    }

}
