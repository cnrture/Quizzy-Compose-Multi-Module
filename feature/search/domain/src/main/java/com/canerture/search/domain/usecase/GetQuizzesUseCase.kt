package com.canerture.search.domain.usecase

import com.canerture.search.domain.repository.SearchRepository
import javax.inject.Inject

class GetQuizzesUseCase @Inject constructor(
    private val repository: SearchRepository,
) {
    suspend operator fun invoke() = repository.getQuizzes()
}