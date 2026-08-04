package com.canerture.detail.domain.usecase

import com.canerture.detail.domain.repository.DetailRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repository: DetailRepository,
) {
    suspend operator fun invoke(id: Int): Result<String> = repository.addFavorite(id)
}