package com.canerture.home.domain.repository

import com.canerture.core.common.Resource
import com.canerture.home.domain.model.CategoryModel

interface HomeRepository {
    suspend fun getCategories(): Resource<List<CategoryModel>>
}