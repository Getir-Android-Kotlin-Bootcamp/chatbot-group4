package com.getir.patika.chatapp.data

import com.getir.patika.chatapp.data.model.MessageResponse

interface GeminiRepository {
    suspend fun getMessage(history: List<String>, message: String): Result<MessageResponse>
}
