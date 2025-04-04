package com.example.examplechat.data.repository

import com.ably.chat.ChatClient
import com.ably.chat.Message
import com.ably.chat.OrderBy
import com.ably.chat.PaginatedResult
import com.ably.chat.Room
import com.ably.chat.RoomOptions
import com.ably.chat.TypingOptions
import com.example.examplechat.domain.model.ChatMessage
import com.example.examplechat.domain.model.ChatMessageListener
import com.example.examplechat.domain.model.MessageAction
import com.example.examplechat.domain.model.PaginatedMessages
import com.example.examplechat.domain.model.TypingListener
import com.example.examplechat.domain.repository.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AblyChatRepositoryImpl @Inject constructor(
    private val chatClient: ChatClient
): ChatRepository {
    private lateinit var room: Room
    private lateinit var pagMess: PaginatedResult<Message>

    override suspend fun initializeRoom(roomId: String) {
        room = chatClient.rooms.get(
            roomId = roomId,
            options = RoomOptions(
                typing = TypingOptions(timeoutMs = 5_000)
            )
        )
        room.attach()
    }

    override fun getClientId(): String {
        return chatClient.clientId
    }

    override suspend fun sendMessage(message: String) {
        room.messages.send(message)
    }

    override suspend fun streamMessages(listener: ChatMessageListener) {
        room.messages.subscribe { sdkMessage ->
            listener.onNewMessage(sdkMessage.message.toChatMessage())
        }
    }

    override suspend fun getMessages(): PaginatedMessages {
        pagMess = room.messages.get(orderBy = OrderBy.NewestFirst)
        return pagMess.toPaginatedMessages()
    }

    override suspend fun getPreviousMessages(): PaginatedMessages {
        pagMess = pagMess.next()
        return pagMess.toPaginatedMessages()
    }


    override suspend fun streamTyping(listener: TypingListener) {
        room.typing.subscribe { event ->
            listener.onTyping(event.currentlyTyping)
        }
    }

    override suspend fun startTyping() {
        room.typing.start()
    }

    override suspend fun stopTyping() {
        room.typing.stop()
    }

    private fun Message.toChatMessage(): ChatMessage {
        return ChatMessage(
            serial = this.serial,
            clientId = this.clientId,
            roomId = this.roomId,
            text = this.text,
            createdAt = this.createdAt,
            action = when (this.action.name.lowercase()) {
                "created" -> MessageAction.Created
                "edited" -> MessageAction.Edited
                "deleted" -> MessageAction.Deleted
                else -> MessageAction.Created // fallback
            },
            timestamp = this.timestamp
        )
    }

    private fun PaginatedResult<Message>.toPaginatedMessages(): PaginatedMessages {
        val messages = this.items.map { it.toChatMessage() }

        return PaginatedMessages(
            items = messages,
            hasNext = this.hasNext(),
            next = if (this.hasNext()) {
                suspend { this.next().toPaginatedMessages() }
            } else null
        )
    }
}