package com.canerture.datasource.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Singleton

@Singleton
class ProfileDataSource {

    private val profileData = MutableSharedFlow<ProfileModel>(1)

    init {
        profileData.tryEmit(ProfileModel("", ""))
    }

    fun save(value: ProfileModel) = profileData.tryEmit(value)

    fun get(): Flow<ProfileModel> = profileData.asSharedFlow()
}