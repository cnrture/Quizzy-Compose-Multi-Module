package com.canerture.favorites.data.repository

import com.canerture.favorites.data.mapper.toModel
import com.canerture.favorites.data.source.FavoritesApi
import com.canerture.favorites.domain.model.FavoriteModel
import com.canerture.favorites.domain.repository.FavoritesRepository
import com.canerture.network.safeApiCall
import javax.inject.Inject

internal class FavoritesRepositoryImpl @Inject constructor(
    private val api: FavoritesApi,
) : FavoritesRepository {

    override suspend fun getFavorites(): Result<List<FavoriteModel>> {
        return safeApiCall { api.getFavorites() }.map { it.data.toModel() }
    }

    override suspend fun deleteFavorite(id: Int): Result<Unit> {
        return safeApiCall { api.deleteFavorite(id) }
    }
}