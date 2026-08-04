package com.canerture.quiz.domain.usecase

import com.canerture.quiz.domain.model.QuizModel
import com.canerture.quiz.domain.repository.QuizRepository
import javax.inject.Inject

class GetQuizUseCase @Inject constructor(
    private val repository: QuizRepository,
) {
    suspend operator fun invoke(id: Int): Result<QuizModel> = repository.getQuiz(id)
}