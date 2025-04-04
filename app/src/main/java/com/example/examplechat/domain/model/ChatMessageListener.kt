package com.example.examplechat.domain.model

fun interface ChatMessageListener {
    fun onNewMessage(message: ChatMessage)
}