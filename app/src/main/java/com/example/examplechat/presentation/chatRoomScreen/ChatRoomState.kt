package com.example.examplechat.presentation.chatRoomScreen

import com.example.examplechat.domain.model.ChatMessage

data class ChatRoomState(
    val messages: List<ChatMessage> = emptyList(),
    val chatItems: List<ChatItem> = emptyList(),
    val messageText: String = "",
    val expandedMessageId: String? = null,
    val isLoadingMore: Boolean = false,
    val canLoadMoreMessages: Boolean = true,
    val isTypingIndicatorActive: Boolean = false,
    val shouldScrollToBottom: Boolean = false
)
