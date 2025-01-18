package com.canerture.home.data.source

import com.canerture.home.data.common.Constants.CATEGORIES
import com.canerture.home.data.common.Constants.QUIZZES
import com.canerture.home.data.common.Constants.SEARCH
import com.canerture.home.data.model.CategoriesResponse
import com.canerture.home.data.model.PopularQuizzesResponse
import com.canerture.network.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {
    @GET(CATEGORIES)
    suspend fun getCategories(): BaseResponse<List<CategoriesResponse>?>

    @GET(QUIZZES)
    suspend fun getQuizzes(): BaseResponse<List<PopularQuizzesResponse>?>

    @GET(SEARCH)
    suspend fun search(@Query("query") query: String): BaseResponse<List<PopularQuizzesResponse>?>
}