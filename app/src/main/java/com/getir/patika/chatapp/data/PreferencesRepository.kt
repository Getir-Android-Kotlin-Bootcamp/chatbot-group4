package com.getir.patika.chatapp.data

/**
 * Repository interface for managing preferences-related data operations.
 */
interface PreferencesRepository {

    /**
     * Sets the state indicating whether the first message has been sent.
     *
     * @param state The state to be set.
     */
    suspend fun setFirstMessageState(state: Boolean)

    /**
     * Retrieves the state indicating whether the first message has been sent.
     *
     * @return The state indicating whether the first message has been sent.
     */
    suspend fun getFirstMessageState(): Boolean
}
