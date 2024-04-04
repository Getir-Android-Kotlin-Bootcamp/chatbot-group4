package com.getir.patika.chatapp.data.model

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val role: Role = Role.MODEL,
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isLoaded: Boolean = false
)

enum class Role { USER, MODEL }

fun Message.toMessageEntity() = MessageEntity(
    messageId = id,
    role = role,
    message = message,
    timestamp = timestamp,
    isLoaded = isLoaded
)
