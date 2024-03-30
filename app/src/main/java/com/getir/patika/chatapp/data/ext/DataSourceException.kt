package com.getir.patika.chatapp.data.ext

sealed class DataSourceException(message: String) : Exception(message) {
    class GeminiTextNotFoundException : DataSourceException("Gemini text not found")
}
