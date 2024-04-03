package com.getir.patika.chatapp.data.impl

import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.data.GeminiRepository
import com.getir.patika.chatapp.data.ext.runCatchingWithContext
import com.getir.patika.chatapp.data.model.Message
import com.getir.patika.chatapp.data.model.MessageEntity
import com.getir.patika.chatapp.data.model.Role
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
            val userMessageEntity = Message(message = message, role = Role.USER).toMessageEntity()
            chatDao.insertMessage(userMessageEntity)

            val recentMessages = chatDao.getLastMessages().map(MessageEntity::toContent)
            println("Recent messages: ${recentMessages[0].role}")
            val response = geminiRepository.getResponse(recentMessages, message).getOrThrow()
            println("Response: $response")
            val modelMessageEntity = Message(message = response).toMessageEntity()
            chatDao.insertMessage(modelMessageEntity)
        }

    override fun getAllMessages(): Flow<List<Message>> =
        chatDao.getAllMessages().map { messages -> messages.map(MessageEntity::toMessage) }
}
