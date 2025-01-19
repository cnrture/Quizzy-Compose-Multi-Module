package com.canerture.category.data.source

import com.canerture.category.data.common.Constants.QUIZZES
import com.canerture.home.data.model.QuizResponse
import com.canerture.network.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryApi {
    @GET(QUIZZES)
    suspend fun getQuizzesByCategory(@Query("categoryId") categoryId: Int): BaseResponse<List<QuizResponse>?>
}