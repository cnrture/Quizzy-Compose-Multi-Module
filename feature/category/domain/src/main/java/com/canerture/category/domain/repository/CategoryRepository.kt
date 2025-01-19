package com.canerture.category.domain.repository

import com.canerture.category.domain.model.QuizModel
import com.canerture.core.common.Resource

interface CategoryRepository {
    suspend fun getQuizzesByCategory(categoryId: Int): Resource<List<QuizModel>>
}