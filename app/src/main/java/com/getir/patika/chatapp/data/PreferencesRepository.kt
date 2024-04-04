package com.getir.patika.chatapp.data

interface PreferencesRepository {
    suspend fun setFirstMessageState(state: Boolean)
    suspend fun getFirstMessageState(): Boolean
}
