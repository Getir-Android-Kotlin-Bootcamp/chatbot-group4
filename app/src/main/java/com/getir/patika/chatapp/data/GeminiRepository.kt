package com.getir.patika.chatapp.data

interface GeminiRepository {
    suspend fun getResponse(message: String): Result<String>
}
