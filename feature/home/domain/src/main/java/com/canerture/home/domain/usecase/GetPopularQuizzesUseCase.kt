package com.canerture.home.domain.usecase

import com.canerture.home.domain.model.PopularQuizModel
import com.canerture.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetPopularQuizzesUseCase @Inject constructor(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(): Result<List<PopularQuizModel>> = repository.getPopularQuizzes()
}