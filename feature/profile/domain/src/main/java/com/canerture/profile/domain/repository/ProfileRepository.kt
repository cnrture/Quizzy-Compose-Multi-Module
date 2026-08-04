package com.canerture.profile.domain.repository

import com.canerture.profile.domain.model.ProfileModel
import com.canerture.profile.domain.model.RankModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Result<ProfileModel>>
    suspend fun getRank(): Result<RankModel>
    suspend fun logout()
}