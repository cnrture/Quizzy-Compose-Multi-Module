package com.canerture.favorites.domain.usecase

import com.canerture.favorites.domain.repository.FavoritesRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepository,
) {
    suspend operator fun invoke(id: Int): Result<Unit> = repository.deleteFavorite(id)
}