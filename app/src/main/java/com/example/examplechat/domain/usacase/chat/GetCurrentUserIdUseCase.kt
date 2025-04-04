package com.example.examplechat.domain.usacase.chat

import com.example.examplechat.domain.repository.ChatRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): String {
        return chatRepository.getClientId()
    }
}