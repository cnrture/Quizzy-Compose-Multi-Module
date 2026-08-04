package com.canerture.detail.domain.repository

import com.canerture.detail.domain.model.QuizDetailModel

interface DetailRepository {
    suspend fun getQuizDetail(id: Int): Result<QuizDetailModel>
    suspend fun addFavorite(id: Int): Result<String>
    suspend fun deleteFavorite(id: Int): Result<String>
}