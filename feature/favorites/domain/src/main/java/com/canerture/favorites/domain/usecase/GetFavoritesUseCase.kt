package com.canerture.favorites.domain.usecase

import com.canerture.favorites.domain.model.FavoriteModel
import com.canerture.favorites.domain.repository.FavoritesRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoritesRepository,
) {
    suspend operator fun invoke(): Result<List<FavoriteModel>> = repository.getFavorites()
}