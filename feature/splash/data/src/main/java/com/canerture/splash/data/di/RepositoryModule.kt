package com.canerture.splash.data.di

import com.canerture.splash.data.repository.SplashRepositoryImpl
import com.canerture.splash.domain.repository.SplashRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSplashRepository(repository: SplashRepositoryImpl): SplashRepository
}