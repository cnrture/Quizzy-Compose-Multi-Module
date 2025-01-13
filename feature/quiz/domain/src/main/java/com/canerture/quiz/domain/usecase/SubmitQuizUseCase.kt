package com.canerture.quiz.domain.usecase

import com.canerture.quiz.domain.repository.QuizRepository
import javax.inject.Inject

class SubmitQuizUseCase @Inject constructor(
    private val repository: QuizRepository,
) {
    suspend operator fun invoke(quizId: Int, score: Int) = repository.submitQuiz(quizId, score)
}