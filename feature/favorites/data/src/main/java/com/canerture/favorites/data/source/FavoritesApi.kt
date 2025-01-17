package com.canerture.favorites.data.source

import com.canerture.favorites.data.common.Constants.FAVORITES
import com.canerture.favorites.data.model.FavoriteResponse
import com.canerture.network.model.BaseResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface FavoritesApi {
    @GET(FAVORITES)
    suspend fun getFavorites(): BaseResponse<List<FavoriteResponse>>

    @DELETE(FAVORITES)
    suspend fun deleteFavorite(@Query("id") id: Int): BaseResponse<List<FavoriteResponse>>
}