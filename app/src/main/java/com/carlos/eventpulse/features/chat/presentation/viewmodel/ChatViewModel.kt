package com.carlos.eventpulse.features.chat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlos.eventpulse.core.util.Resource
import com.carlos.eventpulse.features.chat.domain.model.Message
import com.carlos.eventpulse.features.chat.domain.repositories.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSending: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadHistorial()
        observeMessages()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            repository.observeMessages()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { messages ->
                    _uiState.update { it.copy(messages = messages) }
                }
        }
    }

    private fun loadHistorial() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = repository.loadHistorial()) {
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun sendMessage(contenido: String) {
        if (contenido.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true) }
            when (val result = repository.sendMessage(contenido)) {
                is Resource.Error -> _uiState.update { it.copy(isSending = false, error = result.message) }
                else -> _uiState.update { it.copy(isSending = false) }
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
}
