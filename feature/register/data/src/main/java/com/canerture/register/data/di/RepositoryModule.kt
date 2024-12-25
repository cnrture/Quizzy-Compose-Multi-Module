package com.canerture.register.data.di

import com.canerture.register.data.repository.RegisterRepositoryImpl
import com.canerture.register.domain.repository.RegisterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRegisterRepository(repository: RegisterRepositoryImpl): RegisterRepository
}