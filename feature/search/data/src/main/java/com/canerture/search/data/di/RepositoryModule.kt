package com.canerture.search.data.di

import com.canerture.search.data.repository.SearchRepositoryImpl
import com.canerture.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindSearchRepository(repository: SearchRepositoryImpl): SearchRepository
}