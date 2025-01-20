package com.canerture.detail.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.detail.data.mapper.toModel
import com.canerture.detail.data.model.AddFavoriteRequest
import com.canerture.detail.data.source.DetailApi
import com.canerture.detail.domain.model.QuizDetailModel
import com.canerture.detail.domain.repository.DetailRepository
import com.canerture.network.safeApiCall
import javax.inject.Inject

internal class DetailRepositoryImpl @Inject constructor(
    private val api: DetailApi,
) : DetailRepository {
    override suspend fun getQuizDetail(id: Int): Resource<QuizDetailModel> {
        return safeApiCall { api.getQuizDetail(id) }.map { it.data.toModel() }
    }

    override suspend fun addFavorite(id: Int): Resource<String> {
        val request = AddFavoriteRequest(id)
        return safeApiCall { api.addFavorite(request) }.map { it.message.orEmpty() }
    }

    override suspend fun deleteFavorite(id: Int): Resource<String> {
        return safeApiCall { api.removeFavorite(id) }.map { it.message.orEmpty() }
    }
}