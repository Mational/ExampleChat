package com.example.examplechat.domain.model

data class PaginatedMessages(
    val items: List<ChatMessage>,
    val hasNext: Boolean,
    val next: (suspend () -> PaginatedMessages)? = null
)