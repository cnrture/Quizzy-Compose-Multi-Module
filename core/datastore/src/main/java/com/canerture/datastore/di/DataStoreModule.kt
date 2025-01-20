package com.canerture.datastore.di

import android.content.Context
import com.canerture.datastore.DataStoreHelper
import com.canerture.datastore.DataStoreHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStoreHelper(
        @ApplicationContext context: Context,
    ): DataStoreHelper = DataStoreHelperImpl(context)
}