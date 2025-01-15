package com.canerture.home.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.datasource.profile.ProfileDataSource
import com.canerture.home.data.mapper.toModel
import com.canerture.home.data.source.HomeApi
import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.model.PopularQuizModel
import com.canerture.home.domain.repository.HomeRepository
import com.canerture.network.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi,
    private val profileDataSource: ProfileDataSource,
) : HomeRepository {
    override suspend fun getCategories(): Resource<List<CategoryModel>> {
        return safeApiCall { api.getCategories() }.map { it.data.toModel() }
    }

    override suspend fun getPopularQuizzes(): Resource<List<PopularQuizModel>> {
        return safeApiCall { api.getQuizzes() }.map { it.data.toModel() }
    }

    override fun getUsername(): Flow<String> {
        return profileDataSource.get().map { it.username }
    }
}