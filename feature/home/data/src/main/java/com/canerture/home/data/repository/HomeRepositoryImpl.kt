package com.canerture.home.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.home.data.mapper.toModel
import com.canerture.home.data.source.HomeApi
import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.repository.HomeRepository
import com.canerture.network.safeApiCall
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi,
) : HomeRepository {
    override suspend fun getCategories(): Resource<List<CategoryModel>> {
        return safeApiCall { api.getCategories() }.map {
            it.toModel()
        }
    }
}