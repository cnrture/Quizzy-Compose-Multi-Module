package com.canerture.home.domain.repository

import com.canerture.core.common.Resource
import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.model.PopularQuizModel

interface HomeRepository {
    suspend fun getCategories(): Resource<List<CategoryModel>>
    suspend fun getPopularQuizzes(): Resource<List<PopularQuizModel>>
}