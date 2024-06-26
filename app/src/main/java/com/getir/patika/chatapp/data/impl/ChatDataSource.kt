package com.getir.patika.chatapp.data.impl

import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.data.GeminiRepository
import com.getir.patika.chatapp.data.PreferencesRepository
import com.getir.patika.chatapp.data.ext.runCatchingWithContext
import com.getir.patika.chatapp.data.model.Message
import com.getir.patika.chatapp.data.model.MessageEntity
import com.getir.patika.chatapp.data.model.Role
import com.getir.patika.chatapp.data.model.toMessage
import com.getir.patika.chatapp.data.model.toMessageEntity
import com.getir.patika.chatapp.data.room.ChatDao
import com.getir.patika.chatapp.util.Utils.MODEL_PRE_TEXT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [ChatRepository] interface.
 */
class ChatDataSource @Inject constructor(
    private val chatDao: ChatDao,
    private val geminiRepository: GeminiRepository,
    private val preferencesRepository: PreferencesRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ChatRepository {
    /**
     * Sends a message to the chat.
     */
    override suspend fun sendMessage(message: String): Result<Unit> =
        runCatchingWithContext(ioDispatcher) {
            val modelMessage = Message().toMessageEntity()

            val modelMessageId = chatDao.insertMessage(modelMessage).toInt()

            val response = geminiRepository.getResponse(message).getOrElse {
                chatDao.updateMessage(modelMessageId, it.message ?: GENERIC_ERROR, true)
                throw it
            }

            chatDao.updateMessage(modelMessageId, response, true)
        }

    /**
     * Saves a message to the database.
     */
    override suspend fun saveMessageToDb(message: String): Result<Unit> =
        runCatchingWithContext(ioDispatcher) {
            Message(message = message, role = Role.USER, isLoaded = true).toMessageEntity().let {
                chatDao.insertMessage(it)
            }
        }

    /**
     * Retrieves all messages from the database.
     */
    override fun getAllMessages(): Flow<List<Message>> =
        chatDao.getAllMessages().map { messages -> messages.map(MessageEntity::toMessage) }

    /**
     * Sends a model message for the first time.
     */
    override suspend fun sendModelMessageForFirstTime() {
        val firstMessageState = preferencesRepository.getFirstMessageState()
        if (firstMessageState) return
        val modelMessageEntity = Message(message = MODEL_PRE_TEXT, isLoaded = true).toMessageEntity()
        chatDao.insertMessage(modelMessageEntity)
        preferencesRepository.setFirstMessageState(true)
    }

    companion object {
        private const val GENERIC_ERROR = "Something went wrong. Please try again."
    }
}
