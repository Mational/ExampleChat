package com.example.examplechat.domain.model

fun interface TypingListener {
    fun onTyping(usersTyping: Set<String>)
}