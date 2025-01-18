package com.canerture.favorites.domain.usecase

import com.canerture.core.common.Resource
import com.canerture.favorites.domain.model.FavoriteModel
import com.canerture.favorites.domain.repository.FavoritesRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepository,
) {
    suspend operator fun invoke(id: Int): Resource<Unit> = repository.deleteFavorite(id)
}