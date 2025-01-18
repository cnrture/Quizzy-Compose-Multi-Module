package com.canerture.favorites.domain.repository

import com.canerture.core.common.Resource
import com.canerture.favorites.domain.model.FavoriteModel

interface FavoritesRepository {
    suspend fun getFavorites(): Resource<List<FavoriteModel>>
    suspend fun deleteFavorite(id: Int): Resource<Unit>
}