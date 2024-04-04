package com.getir.patika.chatapp.data

import com.getir.patika.chatapp.data.model.Message
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing chat-related data operations.
 */
interface ChatRepository {

    /**
     * Sends a message to the chat.
     *
     * @param message The message to be sent.
     * @return Result of the operation.
     */
    suspend fun sendMessage(message: String): Result<Unit>

    /**
     * Saves a message to the local database.
     *
     * @param message The message to be saved.
     * @return Result of the operation.
     */
    suspend fun saveMessageToDb(message: String): Result<Unit>

    /**
     * Retrieves all messages from the chat.
     *
     * @return A flow emitting a list of messages.
     */
    fun getAllMessages(): Flow<List<Message>>

    /**
     * Sends a model message for the first time, for initialization.
     */
    suspend fun sendModelMessageForFirstTime()
}
