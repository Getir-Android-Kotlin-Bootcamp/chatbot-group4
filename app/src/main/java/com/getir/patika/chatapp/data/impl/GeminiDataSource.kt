package com.getir.patika.chatapp.data.impl

import android.util.Log
import com.getir.patika.chatapp.data.GeminiRepository
import com.getir.patika.chatapp.data.ext.DataSourceException.GeminiTextNotFoundException
import com.getir.patika.chatapp.data.ext.runCatchingWithContext
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GeminiDataSource @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val ioDispatcher: CoroutineDispatcher
) : GeminiRepository {
    override suspend fun getResponse(message: String): Result<String> =
        runCatchingWithContext(ioDispatcher) {
            val chat = generativeModel.startChat()
            chat.sendMessage(message).text ?: throw GeminiTextNotFoundException()
        }
}
