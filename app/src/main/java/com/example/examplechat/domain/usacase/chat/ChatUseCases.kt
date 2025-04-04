package com.example.examplechat.domain.usacase.chat

import javax.inject.Inject

class ChatUseCases @Inject constructor(
    val initializeRoomUseCase: InitializeRoomUseCase,
    val sendMessageUseCase: SendMessageUseCase,
    val getMessagesUseCase: GetMessagesUseCase,
    val streamMessagesUseCase: StreamMessagesUseCase,
    val typingIndicatorUseCase: TypingIndicatorUseCase,
    val loadPreviousMessagesUseCase: LoadPreviousMessagesUseCase,
    val startEmitTypingIndicatorUseCase: StartEmitTypingIndicatorUseCase,
    val stopEmitTypingIndicatorUseCase: StopEmitTypingIndicatorUseCase,
    val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
)