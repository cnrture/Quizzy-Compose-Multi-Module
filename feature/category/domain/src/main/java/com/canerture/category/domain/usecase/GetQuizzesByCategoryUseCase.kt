package com.canerture.category.domain.usecase

import com.canerture.category.domain.model.QuizModel
import com.canerture.category.domain.repository.CategoryRepository
import javax.inject.Inject

class GetQuizzesByCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository,
) {
    suspend operator fun invoke(categoryId: Int): Result<List<QuizModel>> =
        repository.getQuizzesByCategory(categoryId)
}