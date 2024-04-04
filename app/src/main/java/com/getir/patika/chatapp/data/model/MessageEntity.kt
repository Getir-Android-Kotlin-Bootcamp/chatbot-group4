package com.getir.patika.chatapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val messageId: String,
    val role: Role,
    val message: String,
    val timestamp: Long,
    val isLoaded: Boolean
)

fun MessageEntity.toMessage(): Message =
    Message(
        id = this.messageId,
        role = this.role,
        message = this.message,
        timestamp = this.timestamp,
        isLoaded = isLoaded
    )
