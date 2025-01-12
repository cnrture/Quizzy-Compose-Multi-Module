package com.canerture.connectivity.di

import android.content.Context
import com.canerture.connectivity.ConnectivityListener
import com.canerture.connectivity.ConnectivityListenerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    @Singleton
    fun provideConnectivityListener(
        @ApplicationContext context: Context,
    ): ConnectivityListener = ConnectivityListenerImpl(context)
}