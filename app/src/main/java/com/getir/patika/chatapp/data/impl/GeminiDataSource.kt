package com.getir.patika.chatapp.data.impl

import com.getir.patika.chatapp.data.GeminiRepository
import com.getir.patika.chatapp.data.ext.DataSourceException.GeminiTextNotFoundException
import com.getir.patika.chatapp.data.ext.runCatchingWithContext
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GeminiDataSource @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val ioDispatcher: CoroutineDispatcher
) : GeminiRepository {
    override suspend fun getResponse(
        history: List<Content>,
        message: String
    ): Result<String> = runCatchingWithContext(ioDispatcher) {

        val chat = generativeModel.startChat(history = history)

        val response = chat.sendMessage(message)

        response.text ?: throw GeminiTextNotFoundException()
    }
}
