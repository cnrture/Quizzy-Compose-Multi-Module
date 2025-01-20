package com.canerture.datasource.logout

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataSourceModule {

    @Provides
    @Singleton
    fun provideLogoutDatasource(): LogoutDataSource {
        return LogoutDataSource()
    }
}