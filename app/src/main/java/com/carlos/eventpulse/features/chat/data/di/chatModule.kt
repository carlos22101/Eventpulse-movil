package com.carlos.eventpulse.features.chat.data.di

import com.carlos.eventpulse.core.di.AuthenticatedClient
import com.carlos.eventpulse.features.chat.data.remote.ChatApiService
import com.carlos.eventpulse.features.chat.data.repositories.ChatRepositoryImpl
import com.carlos.eventpulse.features.chat.domain.repositories.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    companion object {
        @Provides
        @Singleton
        fun provideChatApiService(
            @AuthenticatedClient retrofit: Retrofit
        ): ChatApiService = retrofit.create(ChatApiService::class.java)
    }
}
