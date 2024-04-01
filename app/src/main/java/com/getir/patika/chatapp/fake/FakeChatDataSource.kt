package com.getir.patika.chatapp.fake

import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.data.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeChatDataSource(private val shouldFail: Boolean = false) : ChatRepository {
    private val messages = mutableListOf<Message>()

    override suspend fun sendMessage(message: String): Result<Unit> {
        return if (shouldFail) {
            Result.failure(RuntimeException("Failed to send message"))
        } else {
            messages.add(Message(message = message))
            Result.success(Unit)
        }
    }

    override suspend fun getAllMessages(): Result<Flow<List<Message>>> {
        return if (shouldFail) {
            Result.failure(RuntimeException("Failed to retrieve messages"))
        } else {
            Result.success(flow { emit(messages.toList()) })
        }
    }
}
