package com.canerture.editprofile.domain.usecase

import com.canerture.core.common.Resource
import com.canerture.editprofile.domain.model.AvatarModel
import com.canerture.editprofile.domain.repository.EditProfileRepository
import javax.inject.Inject

class GetAvatarsUseCase @Inject constructor(
    private val repository: EditProfileRepository,
) {
    suspend operator fun invoke(): Resource<List<AvatarModel>> = repository.getAvatars()
}