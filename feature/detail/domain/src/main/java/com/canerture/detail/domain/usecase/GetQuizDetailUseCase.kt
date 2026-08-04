package com.canerture.detail.domain.usecase

import com.canerture.detail.domain.model.QuizDetailModel
import com.canerture.detail.domain.repository.DetailRepository
import javax.inject.Inject

class GetQuizDetailUseCase @Inject constructor(
    private val repository: DetailRepository,
) {
    suspend operator fun invoke(id: Int): Result<QuizDetailModel> = repository.getQuizDetail(id)
}