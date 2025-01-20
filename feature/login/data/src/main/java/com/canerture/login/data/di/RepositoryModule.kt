package com.canerture.login.data.di

import com.canerture.login.data.repository.LoginRepositoryImpl
import com.canerture.login.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindLoginRepository(repository: LoginRepositoryImpl): LoginRepository
}