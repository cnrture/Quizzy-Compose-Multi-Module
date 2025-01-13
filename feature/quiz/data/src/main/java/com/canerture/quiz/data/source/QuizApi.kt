package com.canerture.quiz.data.source

import com.canerture.network.model.BaseResponse
import com.canerture.quiz.data.common.Constants.QUIZ
import com.canerture.quiz.data.model.QuizResponse
import com.canerture.quiz.data.model.SubmitQuizRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface QuizApi {
    @GET(QUIZ)
    suspend fun getQuiz(@Query("id") id: Int): BaseResponse<QuizResponse>

    @POST(QUIZ)
    suspend fun submitQuiz(@Body request: SubmitQuizRequest): BaseResponse<Int>
}