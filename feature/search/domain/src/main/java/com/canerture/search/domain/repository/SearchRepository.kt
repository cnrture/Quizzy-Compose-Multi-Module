package com.canerture.search.domain.repository

import com.canerture.core.common.Resource
import com.canerture.search.domain.model.QuizModel

interface SearchRepository {
    suspend fun searchQuiz(query: String): Resource<List<QuizModel>>
}