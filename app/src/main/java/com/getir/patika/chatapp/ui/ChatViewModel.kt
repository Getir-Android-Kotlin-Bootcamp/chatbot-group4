package com.getir.patika.chatapp.ui

import androidx.lifecycle.viewModelScope
import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val query: String = "",
    val isTextFieldEnabled: Boolean = true
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _messageState = MutableStateFlow(listOf<ChatItem>())
    val messageState = _messageState.asStateFlow()

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun observeMessages() = launchCatching {
        chatRepository.getAllMessages()
            .map { it.map<Message, ChatItem> { message: Message -> ChatItem.ChatMessage(message) } }
            .collectLatest { chatItems ->
                val lastItem = _messageState.value.lastOrNull()
                if (lastItem is ChatItem.Loading) {
                    _messageState.update { it.dropLast(1) }
                }
                _messageState.update { chatItems }
            }
    }

    fun getFirstMessageState() = launchCatching {
        chatRepository.sendModelMessageForFirstTime()
    }

    fun onSend(message: String) {
        _uiState.update { it.copy(isTextFieldEnabled = false) }
        viewModelScope.launch {
            chatRepository.saveMessageToDb(message).getOrThrow()

            _messageState.update { it + ChatItem.Loading }

            delay(3000L)

            chatRepository.sendMessage(message)
                .onFailure {
                    _messageState.update { it.dropLast(1) }
                }

            _uiState.update { it.copy(isTextFieldEnabled = true) }
        }
    }
}
