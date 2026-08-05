package com.canerture.search.data.source

import com.canerture.network.model.BaseResponse
import com.canerture.search.data.common.Constants.QUIZZES
import com.canerture.search.data.model.QuizResponse
import retrofit2.http.GET

internal interface SearchApi {
    @GET(QUIZZES)
    suspend fun getQuizzes(): BaseResponse<List<QuizResponse>>
}