package com.canerture.profile.domain.repository

import com.canerture.core.common.Resource
import com.canerture.profile.domain.model.ProfileModel
import com.canerture.profile.domain.model.RankModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Resource<ProfileModel>>
    suspend fun getRank(): Resource<RankModel>
    suspend fun logout()
}