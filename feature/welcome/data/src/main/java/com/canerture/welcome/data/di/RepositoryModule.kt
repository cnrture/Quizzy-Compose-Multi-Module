package com.canerture.welcome.data.di

import com.canerture.welcome.data.repository.WelcomeRepositoryImpl
import com.canerture.welcome.domain.repository.WelcomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindWelcomeRepository(repository: WelcomeRepositoryImpl): WelcomeRepository
}