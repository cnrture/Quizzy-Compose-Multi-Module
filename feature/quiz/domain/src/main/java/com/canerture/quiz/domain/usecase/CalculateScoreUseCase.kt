package com.canerture.quiz.domain.usecase

import javax.inject.Inject

class CalculateScoreUseCase @Inject constructor() {

    operator fun invoke(
        maxScore: Int,
        correctAnswers: Int,
        totalQuestions: Int,
    ): Int {
        if (totalQuestions <= 0) return 0
        return maxScore * correctAnswers / totalQuestions
    }
}
