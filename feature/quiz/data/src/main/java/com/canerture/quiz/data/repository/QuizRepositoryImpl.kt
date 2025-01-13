package com.canerture.quiz.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.network.safeApiCall
import com.canerture.quiz.data.mapper.toModel
import com.canerture.quiz.data.source.QuizApi
import com.canerture.quiz.domain.model.QuizModel
import com.canerture.quiz.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val api: QuizApi,
) : QuizRepository {

    override suspend fun getQuiz(id: Int): Resource<QuizModel> {
        return safeApiCall { api.getQuiz(id) }.map {
            it.toModel()
        }
    }
}