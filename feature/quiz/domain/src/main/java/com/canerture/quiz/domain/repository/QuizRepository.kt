package com.canerture.quiz.domain.repository

import com.canerture.core.common.Resource
import com.canerture.quiz.domain.model.QuizModel

interface QuizRepository {
    suspend fun getQuiz(id: Int): Resource<QuizModel>
    suspend fun submitQuiz(quizId: Int, score: Int): Resource<Unit>
}