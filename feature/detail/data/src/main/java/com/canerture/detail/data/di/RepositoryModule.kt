package com.canerture.detail.data.di

import com.canerture.detail.data.repository.DetailRepositoryImpl
import com.canerture.detail.domain.repository.DetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDetailRepository(repository: DetailRepositoryImpl): DetailRepository
}