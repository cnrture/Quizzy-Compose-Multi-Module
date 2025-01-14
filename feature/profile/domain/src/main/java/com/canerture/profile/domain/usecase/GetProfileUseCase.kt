package com.canerture.profile.domain.usecase

import com.canerture.core.common.Resource
import com.canerture.profile.domain.model.ProfileModel
import com.canerture.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    operator fun invoke(): Flow<Resource<ProfileModel>> = repository.getProfile()
}