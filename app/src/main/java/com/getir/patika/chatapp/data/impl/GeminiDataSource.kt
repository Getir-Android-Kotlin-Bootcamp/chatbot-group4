package com.getir.patika.chatapp.data.impl

import com.getir.patika.chatapp.data.GeminiRepository
import com.getir.patika.chatapp.data.ext.runCatchingWithContext
import com.getir.patika.chatapp.data.model.MessageResponse
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GeminiDataSource @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val ioDispatcher: CoroutineDispatcher
) :
    GeminiRepository {
    override suspend fun getMessage(
        history: List<String>,
        message: String
    ): Result<MessageResponse> = runCatchingWithContext(ioDispatcher) {





        MessageResponse(message = "")
    }
}
