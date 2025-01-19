package com.canerture.category.data.repository

import com.canerture.category.data.mapper.toModel
import com.canerture.category.data.source.CategoryApi
import com.canerture.category.domain.model.QuizModel
import com.canerture.category.domain.repository.CategoryRepository
import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.network.safeApiCall
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api: CategoryApi,
) : CategoryRepository {

    override suspend fun getQuizzesByCategory(categoryId: Int): Resource<List<QuizModel>> {
        return safeApiCall { api.getQuizzesByCategory(categoryId) }.map { it.data.toModel() }
    }
}