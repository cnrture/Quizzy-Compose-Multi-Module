package com.canerture.favorites.domain.repository

import com.canerture.favorites.domain.model.FavoriteModel

interface FavoritesRepository {
    suspend fun getFavorites(): Result<List<FavoriteModel>>
    suspend fun deleteFavorite(id: Int): Result<Unit>
}