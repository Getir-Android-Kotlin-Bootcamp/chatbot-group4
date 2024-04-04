package com.getir.patika.chatapp.data.ext

/**
 * Sealed class representing exceptions related to data sources.
 */
sealed class DataSourceException(message: String) : Exception(message) {
    /**
     * Exception thrown when Gemini text is not found.
     */
    class GeminiTextNotFoundException : DataSourceException("Gemini text not found")
}
