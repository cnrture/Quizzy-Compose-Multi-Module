package com.canerture.search.domain.repository

import com.canerture.search.domain.model.QuizModel

interface SearchRepository {
    suspend fun getQuizzes(): Result<List<QuizModel>>
}