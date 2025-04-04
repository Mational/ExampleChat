package com.example.examplechat.domain.usacase.chat

import com.example.examplechat.domain.repository.ChatRepository
import javax.inject.Inject

class StopEmitTypingIndicatorUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke() {
        chatRepository.stopTyping()
    }
}