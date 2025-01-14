package com.canerture.profile.domain.usecase

import com.canerture.core.common.Resource
import com.canerture.profile.domain.model.RankModel
import com.canerture.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetRankUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(): Resource<RankModel> = repository.getRank()
}