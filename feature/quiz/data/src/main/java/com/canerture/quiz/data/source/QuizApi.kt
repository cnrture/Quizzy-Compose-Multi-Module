package com.canerture.quiz.data.source

import com.canerture.network.model.BaseResponse
import com.canerture.quiz.data.common.Constants.QUIZ
import com.canerture.quiz.data.mapper.QuizResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApi {
    @GET(QUIZ)
    suspend fun getQuiz(@Query("id") id: Int): BaseResponse<List<QuizResponse>>
}