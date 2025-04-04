package com.example.examplechat.domain.usacase.chat

import com.example.examplechat.domain.repository.ChatRepository
import javax.inject.Inject

class StartEmitTypingIndicatorUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke() {
        chatRepository.startTyping()
    }
}