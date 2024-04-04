package com.getir.patika.chatapp.fake

import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.data.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * A fake implementation of [ChatRepository] for testing purposes.
 *
 * @param shouldFail Flag indicating whether operations should simulate failure.
 */
class FakeChatDataSource(private val shouldFail: Boolean = false) : ChatRepository {

    private val messages = mutableListOf<Message>()

    /**
     * Simulates sending a message.
     *
     * @param message The message to be sent.
     * @return Result of the operation.
     */
    override suspend fun sendMessage(message: String): Result<Unit> {
        return if (shouldFail) {
            Result.failure(RuntimeException("Failed to send message"))
        } else {
            messages.add(Message(message = message))
            Result.success(Unit)
        }
    }

    /**
     * Simulates saving a message to the local database.
     *
     * @param message The message to be saved.
     * @return Result of the operation.
     */
    override suspend fun saveMessageToDb(message: String): Result<Unit> {
        return if (shouldFail) {
            Result.failure(RuntimeException("Failed to save message"))
        } else {
            messages.add(Message(message = message))
            Result.success(Unit)
        }
    }

    /**
     * Retrieves all messages.
     *
     * @return A flow emitting a list of messages.
     */
    override fun getAllMessages(): Flow<List<Message>> = flow {
        emit(messages)
    }

    /**
     * Sends a model message for the first time.
     */
    override suspend fun sendModelMessageForFirstTime() {
        messages.add(Message(message = "Hello, how can I help you?"))
    }
}
