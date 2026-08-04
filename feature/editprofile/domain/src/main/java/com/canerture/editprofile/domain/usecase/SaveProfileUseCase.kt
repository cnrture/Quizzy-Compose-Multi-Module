package com.canerture.editprofile.domain.usecase

import com.canerture.editprofile.domain.repository.EditProfileRepository
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val repository: EditProfileRepository,
) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String,
        avatarId: Int,
    ): Result<String> = repository.saveProfile(email, username, password, avatarId)
}