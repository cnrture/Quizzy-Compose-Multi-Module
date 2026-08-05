package com.canerture.search.data.repository

import com.canerture.network.safeApiCall
import com.canerture.search.data.mapper.toModel
import com.canerture.search.data.source.SearchApi
import com.canerture.search.domain.model.QuizModel
import com.canerture.search.domain.repository.SearchRepository
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val api: SearchApi,
) : SearchRepository {

    override suspend fun getQuizzes(): Result<List<QuizModel>> {
        return safeApiCall { api.getQuizzes() }.map { it.data.toModel() }
    }
}