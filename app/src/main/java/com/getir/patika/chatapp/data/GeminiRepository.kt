package com.getir.patika.chatapp.data

import com.google.ai.client.generativeai.type.Content

interface GeminiRepository {
    suspend fun getResponse(history: List<Content>, message: String): Result<String>
}
