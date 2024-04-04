package com.getir.patika.chatapp.data.impl

import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.data.GeminiRepository
import com.getir.patika.chatapp.data.PreferencesRepository
import com.getir.patika.chatapp.data.ext.runCatchingWithContext
import com.getir.patika.chatapp.data.model.Message
import com.getir.patika.chatapp.data.model.MessageEntity
import com.getir.patika.chatapp.data.model.Role
import com.getir.patika.chatapp.data.model.toContent
import com.getir.patika.chatapp.data.model.toMessage
import com.getir.patika.chatapp.data.model.toMessageEntity
import com.getir.patika.chatapp.data.room.ChatDao
import com.getir.patika.chatapp.util.Utils.ModelPreText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatDataSource @Inject constructor(
    private val chatDao: ChatDao,
    private val geminiRepository: GeminiRepository,
    private val preferencesRepository: PreferencesRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ChatRepository {
    override suspend fun sendMessage(message: String): Result<Unit> =
        runCatchingWithContext(ioDispatcher) {
            val response = geminiRepository.getResponse(message).getOrThrow()

            val modelMessageEntity = Message(message = response).toMessageEntity()

            chatDao.insertMessage(modelMessageEntity)
        }

    override suspend fun saveMessageToDb(message: String): Result<Unit> =
        runCatchingWithContext(ioDispatcher) {
            val userMessageEntity = Message(message = message, role = Role.USER).toMessageEntity()
            chatDao.insertMessage(userMessageEntity)
        }

    override fun getAllMessages(): Flow<List<Message>> =
        chatDao.getAllMessages().map { messages -> messages.map(MessageEntity::toMessage) }

    override suspend fun sendModelMessageForFirstTime() {
        val firstMessageState = preferencesRepository.getFirstMessageState()
        if (firstMessageState) return
        val modelMessageEntity = Message(message = ModelPreText).toMessageEntity()
        chatDao.insertMessage(modelMessageEntity)
        preferencesRepository.setFirstMessageState(true)
    }
}
