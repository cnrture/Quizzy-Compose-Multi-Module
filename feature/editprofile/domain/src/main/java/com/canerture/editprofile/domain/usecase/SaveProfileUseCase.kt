package com.canerture.editprofile.domain.usecase

import com.canerture.core.common.Resource
import com.canerture.editprofile.domain.model.ProfileModel
import com.canerture.editprofile.domain.repository.EditProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val repository: EditProfileRepository,
) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String,
        avatarId: Int,
    ): Resource<Unit> = repository.saveProfile(email, username, password, avatarId)
}