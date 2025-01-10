package com.canerture.detail.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.detail.data.mapper.toModel
import com.canerture.detail.data.source.DetailApi
import com.canerture.detail.domain.model.QuizDetailModel
import com.canerture.detail.domain.repository.DetailRepository
import com.canerture.network.safeApiCall
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val api: DetailApi,
) : DetailRepository {
    override suspend fun getQuizDetail(id: Int): Resource<QuizDetailModel> {
        return safeApiCall { api.getQuizDetail(id) }.map {
            it.toModel()
        }
    }
}