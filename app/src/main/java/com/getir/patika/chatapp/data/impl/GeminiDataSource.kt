package com.getir.patika.chatapp.data.impl

import android.util.Log
import com.getir.patika.chatapp.data.GeminiRepository
import com.getir.patika.chatapp.data.ext.DataSourceException.GeminiTextNotFoundException
import com.getir.patika.chatapp.data.ext.runCatchingWithContext
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Implementation of [GeminiRepository] interface.
 */
class GeminiDataSource @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val ioDispatcher: CoroutineDispatcher
) : GeminiRepository {
    /**
     * Gets a response from Gemini based on the given message.
     */
    override suspend fun getResponse(message: String): Result<String> =
        runCatchingWithContext(ioDispatcher) {
            Log.d("GeminiDataSource", "getResponse: $message")
            val chat = generativeModel.startChat()
            chat.sendMessage(message).text ?: throw GeminiTextNotFoundException()
        }
}
