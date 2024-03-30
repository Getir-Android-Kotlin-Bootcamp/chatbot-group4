package com.getir.patika.chatapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val messageId: String,
    val role: Role,
    val message: String,
    val timestamp: Long
)

fun MessageEntity.toMessage(): Message =
    Message(
        id = this.messageId,
        role = this.role,
        message = this.message,
        timestamp = this.timestamp
    )

fun MessageEntity.toContent(): Content =
    content(role = this.role.name.lowercase()) { text(message) }
