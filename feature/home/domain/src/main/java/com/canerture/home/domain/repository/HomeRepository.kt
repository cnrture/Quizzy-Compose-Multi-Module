package com.canerture.home.domain.repository

import com.canerture.core.common.Resource
import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.model.PopularQuizModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getCategories(): Resource<List<CategoryModel>>
    suspend fun getPopularQuizzes(): Resource<List<PopularQuizModel>>
    fun getUsername(): Flow<String>
}