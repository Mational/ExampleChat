package com.example.examplechat.domain.usacase.chat

import com.example.examplechat.domain.model.PaginatedMessages
import com.example.examplechat.domain.repository.ChatRepository
import javax.inject.Inject

class LoadPreviousMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): PaginatedMessages {
        return chatRepository.getPreviousMessages()
    }
}