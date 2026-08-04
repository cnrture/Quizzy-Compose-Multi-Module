package com.canerture.home.domain.repository

import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.model.PopularQuizModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getCategories(): Result<List<CategoryModel>>
    suspend fun getPopularQuizzes(): Result<List<PopularQuizModel>>
    fun getUsername(): Flow<String>
}