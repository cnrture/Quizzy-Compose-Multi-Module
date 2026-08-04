package com.canerture.category.domain.repository

import com.canerture.category.domain.model.QuizModel

interface CategoryRepository {
    suspend fun getQuizzesByCategory(categoryId: Int): Result<List<QuizModel>>
}