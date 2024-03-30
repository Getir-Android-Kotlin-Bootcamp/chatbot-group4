package com.getir.patika.chatapp.data.model

import java.util.UUID

data class MessageResponse(
    val id: String = UUID.randomUUID().toString(),
    val role: Role = Role.MODEL,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class Role { USER, MODEL }
