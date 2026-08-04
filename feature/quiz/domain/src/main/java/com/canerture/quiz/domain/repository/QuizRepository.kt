package com.canerture.quiz.domain.repository

import com.canerture.quiz.domain.model.QuizModel

interface QuizRepository {
    suspend fun getQuiz(id: Int): Result<QuizModel>
    suspend fun submitQuiz(quizId: Int, score: Int): Result<Unit>
}