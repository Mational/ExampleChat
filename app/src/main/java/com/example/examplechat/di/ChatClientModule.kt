package com.example.examplechat.di

import com.ably.chat.ChatClient
import com.example.examplechat.BuildConfig
import com.example.examplechat.data.repository.AblyChatRepositoryImpl
import com.example.examplechat.domain.repository.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ably.lib.realtime.AblyRealtime
import io.ably.lib.types.ClientOptions
import java.util.UUID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatClientModule {
    @Binds
    abstract fun bindChatRepository(
        impl: AblyChatRepositoryImpl
    ): ChatRepository

    companion object {
        @Provides
        @Singleton
        fun provideAblyChatClient(): ChatClient {
            val realtimeClient = AblyRealtime(
                ClientOptions().apply {
                    key = BuildConfig.VITE_ABLY_CHAT_API_KEY
                    this.clientId = UUID.randomUUID().toString()
                }
            )

            return ChatClient(realtimeClient)
        }
    }
}