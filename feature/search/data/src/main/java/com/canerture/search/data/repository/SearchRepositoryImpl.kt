package com.canerture.search.data.repository

import com.canerture.core.common.Resource
import com.canerture.core.common.map
import com.canerture.network.safeApiCall
import com.canerture.search.data.mapper.toModel
import com.canerture.search.data.source.SearchApi
import com.canerture.search.domain.model.QuizModel
import com.canerture.search.domain.repository.SearchRepository
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val api: SearchApi,
) : SearchRepository {

    override suspend fun searchQuiz(query: String): Resource<List<QuizModel>> {
        return safeApiCall { api.searchQuiz(query) }.map { it.data.toModel() }
    }
}