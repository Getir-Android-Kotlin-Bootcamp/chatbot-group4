package com.getir.patika.chatapp.data.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.getir.patika.chatapp.data.PreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>
) : PreferencesRepository {
    override suspend fun setFirstMessageState(state: Boolean) {
        userPreferences.edit { preferences ->
            preferences[MESSAGE_STATE] = state
        }
    }

    override suspend fun getFirstMessageState(): Boolean {
        val preferences = userPreferences.data.first()
        return preferences[MESSAGE_STATE] ?: false
    }

    companion object {
        val MESSAGE_STATE = booleanPreferencesKey("message_state")
    }
}
