package com.canerture.home.data.source

import com.canerture.home.data.common.Constants.CATEGORIES
import com.canerture.home.data.common.Constants.QUIZZES
import com.canerture.home.data.model.CategoriesResponse
import com.canerture.home.data.model.PopularQuizzesResponse
import com.canerture.network.model.BaseResponse
import retrofit2.http.GET

internal interface HomeApi {
    @GET(CATEGORIES)
    suspend fun getCategories(): BaseResponse<List<CategoriesResponse>?>

    @GET(QUIZZES)
    suspend fun getQuizzes(): BaseResponse<List<PopularQuizzesResponse>?>
}