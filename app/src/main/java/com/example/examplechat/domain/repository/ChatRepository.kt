package com.example.examplechat.domain.repository

import com.example.examplechat.domain.model.ChatMessageListener
import com.example.examplechat.domain.model.PaginatedMessages
import com.example.examplechat.domain.model.TypingListener

interface ChatRepository {
    suspend fun initializeRoom(roomId: String)

    fun getClientId(): String

    suspend fun sendMessage(message: String)

    suspend fun streamMessages(listener: ChatMessageListener)
    suspend fun getMessages(): PaginatedMessages
    suspend fun getPreviousMessages(): PaginatedMessages

    suspend fun streamTyping(listener: TypingListener)
    suspend fun startTyping()
    suspend fun stopTyping()
}