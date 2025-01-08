package com.canerture.welcome.data.di

import com.canerture.welcome.data.source.WelcomeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): WelcomeApi {
        return retrofit.create(WelcomeApi::class.java)
    }
}