package com.example.examplechat.domain.model

data class ChatMessage(
    val serial: String,
    val clientId: String,
    val roomId: String,
    val text: String,
    val createdAt: Long,
    val action: MessageAction,
    val timestamp: Long
)

sealed class MessageAction {
    data object Created: MessageAction()
    data object Edited: MessageAction()
    data object Deleted: MessageAction()
}