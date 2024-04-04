package com.getir.patika.chatapp.data

import com.getir.patika.chatapp.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(message: String): Result<Unit>
    suspend fun saveMessageToDb(message: String): Result<Unit>
    fun getAllMessages(): Flow<List<Message>>
    suspend fun sendModelMessageForFirstTime()
}
