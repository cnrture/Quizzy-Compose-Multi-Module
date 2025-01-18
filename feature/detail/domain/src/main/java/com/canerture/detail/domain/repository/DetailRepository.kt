package com.canerture.detail.domain.repository

import com.canerture.core.common.Resource
import com.canerture.detail.domain.model.QuizDetailModel

interface DetailRepository {
    suspend fun getQuizDetail(id: Int): Resource<QuizDetailModel>
    suspend fun addFavorite(id: Int): Resource<String>
    suspend fun deleteFavorite(id: Int): Resource<String>
}