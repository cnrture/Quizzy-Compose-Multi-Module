package com.canerture.search.domain.usecase

import com.canerture.search.domain.repository.SearchRepository
import javax.inject.Inject

class SearchQuizUseCase @Inject constructor(
    private val repository: SearchRepository,
) {
    suspend operator fun invoke(query: String) = repository.searchQuiz(query)
}