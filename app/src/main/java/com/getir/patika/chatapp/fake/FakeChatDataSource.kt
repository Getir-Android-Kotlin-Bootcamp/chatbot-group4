package com.getir.patika.chatapp.fake

import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.data.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

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

    override suspend fun saveMessageToDb(message: String): Result<Unit> {
        return if (shouldFail) {
            Result.failure(RuntimeException("Failed to save message"))
        } else {
            messages.add(Message(message = message))
            Result.success(Unit)
        }
    }

    override fun getAllMessages(): Flow<List<Message>> = flow {
        emit(messages)
    }

    override suspend fun sendModelMessageForFirstTime() {
        messages.add(Message(message = "Hello, how can I help you?"))
    }
}
