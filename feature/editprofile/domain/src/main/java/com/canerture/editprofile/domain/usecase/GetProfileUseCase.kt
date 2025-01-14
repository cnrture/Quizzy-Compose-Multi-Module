package com.canerture.editprofile.domain.usecase

import com.canerture.editprofile.domain.model.ProfileModel
import com.canerture.editprofile.domain.repository.EditProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: EditProfileRepository,
) {
    operator fun invoke(): Flow<ProfileModel> = repository.getProfile()
}