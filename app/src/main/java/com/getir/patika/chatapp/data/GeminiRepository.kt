package com.getir.patika.chatapp.data

/**
 * Repository interface for managing Gemini-related data operations.
 */
interface GeminiRepository {

    /**
     * Retrieves a response from the Gemini service based on the provided message.
     *
     * @param message The message to be sent to Gemini.
     * @return Result of the operation, containing the response string.
     */
    suspend fun getResponse(message: String): Result<String>
}
