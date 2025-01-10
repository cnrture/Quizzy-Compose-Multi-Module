package com.canerture.detail.data.source

import com.canerture.detail.data.common.Constants.QUIZZES
import com.canerture.detail.data.model.QuizDetailResponse
import com.canerture.network.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DetailApi {
    @GET(QUIZZES)
    suspend fun getQuizDetail(@Query("id") id: Int): BaseResponse<QuizDetailResponse>
}