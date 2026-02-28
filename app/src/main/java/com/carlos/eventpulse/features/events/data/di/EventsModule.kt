package com.carlos.eventpulse.features.events.data.di

import com.carlos.eventpulse.core.di.AuthenticatedClient
import com.carlos.eventpulse.features.events.data.remote.EventApiService
import com.carlos.eventpulse.features.events.data.repositories.EventRepositoryImpl
import com.carlos.eventpulse.features.events.domain.repositories.EventRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventsModule {

    @Binds
    @Singleton
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    companion object {
        @Provides
        @Singleton
        fun provideEventApiService(
            @AuthenticatedClient retrofit: Retrofit
        ): EventApiService = retrofit.create(EventApiService::class.java)
    }
}