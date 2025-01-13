package com.canerture.home.domain.usecase

import com.canerture.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetUsernameUseCase @Inject constructor(
    private val repository: HomeRepository,
) {
    operator fun invoke() = repository.getUsername()
}