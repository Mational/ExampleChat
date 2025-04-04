package com.example.examplechat.presentation.chatRoomScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.examplechat.domain.model.ChatMessage
import com.example.examplechat.domain.model.PaginatedMessages
import com.example.examplechat.domain.usacase.chat.ChatUseCases
import com.example.examplechat.presentation.common.BaseViewModel
import com.example.examplechat.utils.formatMessageTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    savedStateHandle: SavedStateHandle
): BaseViewModel<ChatRoomState>(ChatRoomState()) {
    private val roomId: String = savedStateHandle["roomId"] ?: error("Room id is missing")
    private lateinit var pagMessages: PaginatedMessages
    private val currentUserId = chatUseCases.getCurrentUserIdUseCase()

    init {
        viewModelScope.launch {
            initRoom()
            observeTyping()
            observeMessages()
        }
    }

    private suspend fun initRoom() {
        chatUseCases.initializeRoomUseCase(roomId)
        pagMessages = chatUseCases.getMessagesUseCase()
        buildChatItemsWithSeparators(pagMessages.items)
    }

    private suspend fun observeTyping() {
        chatUseCases.typingIndicatorUseCase { currentlyTyping ->
            val isOtherUserTyping = currentlyTyping.any { it != currentUserId }
            updateState { it.copy(isTypingIndicatorActive = isOtherUserTyping) }
            changeVisibilityOfIndicator()
        }
    }

    private suspend fun observeMessages()
    {
        chatUseCases.streamMessagesUseCase { newMessage ->
            onNewMessageReceived(newMessage)
        }
    }


    private fun onNewMessageReceived(newMessage: ChatMessage) {
        val updatedMessages = listOf(newMessage, uiState.value.messages[0])
        buildChatItemsWithSeparators(updatedMessages, reverse = true, isNewMessage = true)

        updateState { it.copy(
            shouldScrollToBottom = newMessage.clientId == currentUserId
        ) }
    }

    fun markScrolledBottom() {
        updateState { it.copy(
            shouldScrollToBottom = false
        ) }
    }

    fun onMessageTextChange(text: String) {
        updateState { it.copy(messageText = text) }

        viewModelScope.launch {
            if (text.isNotBlank()) {
                chatUseCases.startEmitTypingIndicatorUseCase()
            } else {
                chatUseCases.stopEmitTypingIndicatorUseCase()
            }
        }
    }

    fun onExpandToggle(messageId: String) {
        updateState { it.copy(
            expandedMessageId = if (uiState.value.expandedMessageId == messageId) null else messageId
        )}
    }

    fun sendMessage() {
        val content = uiState.value.messageText.trim()
        if (content.isNotBlank()) {
            viewModelScope.launch {
                chatUseCases.sendMessageUseCase(content)
                onMessageTextChange("")
            }
        }
    }

    private fun changeVisibilityOfIndicator() {
        updateState { currentState ->
            val chatItems = currentState.chatItems.filterNot { it is ChatItem.TypingIndicator }.toMutableList()

            if (currentState.isTypingIndicatorActive) {
                chatItems.add(0, ChatItem.TypingIndicator)
            }

            currentState.copy(chatItems = chatItems)
        }
    }

    fun loadPreviousMessages() {
        val updatedChatItems = uiState.value.chatItems
            .toMutableList()
            .apply { add(ChatItem.LoadingItem) }

        updateState {
            it.copy(
                isLoadingMore = true,
                chatItems = updatedChatItems
            )
        }

        viewModelScope.launch {
            val result = chatUseCases.loadPreviousMessagesUseCase()

            if (!result.hasNext) {
                updateState { it.copy(canLoadMoreMessages = false) }
            }

            updateState {
                it.copy(
                    isLoadingMore = false,
                    chatItems = it.chatItems.dropLast(1)
                )
            }

            buildChatItemsWithSeparators(result.items)
        }
    }

    private fun buildChatItemsWithSeparators(
        messages: List<ChatMessage>,
        reverse: Boolean = false,
        isNewMessage: Boolean = false,
    ): List<ChatItem> {
        if (messages.isEmpty()) return emptyList()

        val result = mutableListOf<ChatItem>()

        for (i in  0 until messages.size - 1) {
            result += addFormatedMessageToResult(
                message = messages[i],
                timeDiff = messages[i].timestamp - messages[i+1].timestamp
            )
        }

        if(!isNewMessage) {
            result += addFormatedMessageToResult(
                message = messages.last(),
                isLast = true
            )
        }

        updateState {
            it.copy(
                messages = if (reverse) messages + it.messages else it.messages + messages,
                chatItems = if (reverse) result + it.chatItems
                            else it.chatItems + result
            )
        }

        return result
    }

    private fun addFormatedMessageToResult(
        message: ChatMessage,
        timeDiff: Long = 0L,
        isLast: Boolean = false,
    ): MutableList<ChatItem> {
        val result = mutableListOf<ChatItem>()
        val maxTimeDiff = 15 * 60 * 1000

        result += ChatItem.MessageItem(
            message = message,
            isExpandable = if (isLast) false else timeDiff < maxTimeDiff,
            isOwnMessage = message.clientId == currentUserId
        )

        if (timeDiff >= maxTimeDiff || isLast) {
            result += ChatItem.DateSeparator(formatMessageTimestamp(message.timestamp))
        }

        return result
    }
}