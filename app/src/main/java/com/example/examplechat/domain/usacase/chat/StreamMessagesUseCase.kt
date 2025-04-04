package com.example.examplechat.domain.usacase.chat

import com.example.examplechat.domain.model.ChatMessage
import com.example.examplechat.domain.repository.ChatRepository
import javax.inject.Inject

class StreamMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(onNewMessage: (ChatMessage) -> Unit) {
        chatRepository.streamMessages { message ->
            onNewMessage(message)
        }
    }
}
