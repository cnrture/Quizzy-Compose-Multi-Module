package com.canerture.search.data.source

import com.canerture.network.model.BaseResponse
import com.canerture.search.data.common.Constants.SEARCH
import com.canerture.search.data.model.QuizResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET(SEARCH)
    suspend fun searchQuiz(@Query("query") query: String): BaseResponse<List<QuizResponse>>
}