package com.canerture.leaderboard.data.di

import com.canerture.leaderboard.data.repository.LeaderboardRepositoryImpl
import com.canerture.leaderboard.domain.repository.LeaderboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindLeaderboardRepository(repository: LeaderboardRepositoryImpl): LeaderboardRepository
}