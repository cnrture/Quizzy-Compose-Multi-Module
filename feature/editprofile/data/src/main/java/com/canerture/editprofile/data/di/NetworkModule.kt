package com.canerture.editprofile.data.di

import com.canerture.editprofile.data.source.EditProfileApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideEditProfileApi(retrofit: Retrofit): EditProfileApi {
        return retrofit.create(EditProfileApi::class.java)
    }
}