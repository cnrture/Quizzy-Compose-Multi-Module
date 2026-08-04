package com.canerture.home.domain.usecase

import com.canerture.home.domain.model.CategoryModel
import com.canerture.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(): Result<List<CategoryModel>> = repository.getCategories()
}