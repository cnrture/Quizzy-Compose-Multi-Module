package com.canerture.detail.domain.repository

import com.canerture.core.common.Resource
import com.canerture.detail.domain.model.QuizDetailModel

interface DetailRepository {
    suspend fun getQuizDetail(id: Int): Resource<QuizDetailModel>
}