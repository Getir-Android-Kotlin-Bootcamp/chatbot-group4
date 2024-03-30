package com.getir.patika.chatapp.data

import com.getir.patika.chatapp.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(message: String): Result<Unit>
    suspend fun getAllMessages(): Result<Flow<List<Message>>>
}
