package com.canerture.editprofile.data.di

import com.canerture.editprofile.data.repository.EditProfileRepositoryImpl
import com.canerture.editprofile.domain.repository.EditProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindProfileRepository(editProfileRepositoryImpl: EditProfileRepositoryImpl): EditProfileRepository
}