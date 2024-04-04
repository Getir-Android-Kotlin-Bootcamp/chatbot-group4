package com.getir.patika.chatapp.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.getir.patika.chatapp.data.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val query: String = "",
    val isTextFieldEnabled: Boolean = true
)

/**
 * ViewModel for managing chat-related operations and UI state.
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    val messages = chatRepository
        .getAllMessages()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Updates the query in the UI state.
     */
    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun getFirstMessageState() = launchCatching {
        chatRepository.sendModelMessageForFirstTime()
    }

    /**
     * Sends a message.
     */
    fun onSend(message: String) {
        _uiState.update { it.copy(isTextFieldEnabled = false) }
        viewModelScope.launch {
            chatRepository.saveMessageToDb(message).getOrThrow()

            chatRepository.sendMessage(message).onFailure {
                Log.e("ChatViewModel", "Failed to send message", it)
            }

            _uiState.update { it.copy(isTextFieldEnabled = true) }
        }
    }
}
