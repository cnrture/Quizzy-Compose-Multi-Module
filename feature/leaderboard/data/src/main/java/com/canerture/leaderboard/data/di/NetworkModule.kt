package com.canerture.leaderboard.data.di

import com.canerture.leaderboard.data.source.LeaderboardApi
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
    fun provideLeaderboardApi(retrofit: Retrofit): LeaderboardApi {
        return retrofit.create(LeaderboardApi::class.java)
    }
}