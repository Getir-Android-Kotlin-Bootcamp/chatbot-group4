package com.getir.patika.chatapp.data.impl

import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.data.GeminiRepository
import com.getir.patika.chatapp.data.ext.runCatchingWithContext
import com.getir.patika.chatapp.data.model.Message
import com.getir.patika.chatapp.data.model.MessageEntity
import com.getir.patika.chatapp.data.model.toContent
import com.getir.patika.chatapp.data.model.toMessage
import com.getir.patika.chatapp.data.model.toMessageEntity
import com.getir.patika.chatapp.data.room.ChatDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatDataSource @Inject constructor(
    private val chatDao: ChatDao,
    private val geminiRepository: GeminiRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ChatRepository {
    override suspend fun sendMessage(message: String): Result<Unit> =
        runCatchingWithContext(ioDispatcher) {
            val recentMessages = chatDao.getLastMessages().map(MessageEntity::toContent)

            val response = geminiRepository.getResponse(recentMessages, message).getOrThrow()

            val messageEntity = Message(message = response).toMessageEntity()

            chatDao.insertMessage(messageEntity)
        }

    override suspend fun getAllMessages(): Result<Flow<List<Message>>> =
        runCatchingWithContext(ioDispatcher) {
            chatDao.getAllMessages().map { messages -> messages.map(MessageEntity::toMessage) }
        }
}
