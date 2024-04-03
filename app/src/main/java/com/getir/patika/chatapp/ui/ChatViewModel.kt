package com.getir.patika.chatapp.ui

import androidx.lifecycle.viewModelScope
import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ChatUiState(
    val query: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    val messages: StateFlow<List<Message>> =
        chatRepository
            .getAllMessages()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

    private val query
        get() = uiState.value.query

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    private var sendJob: Job? = null
    fun onSend() {
        _uiState.update { it.copy(isLoading = true) }
        println("Sending message: $query")
        sendJob = launchCatching {
            chatRepository.sendMessage(query).onFailure {
                println("Failed to send message: ${it.message}")
            }
        }
        println("Message sent: $query")
        _uiState.update { it.copy(isLoading = false) }
    }
}
