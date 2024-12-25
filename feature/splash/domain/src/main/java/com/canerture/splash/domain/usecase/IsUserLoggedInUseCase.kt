package com.canerture.splash.domain.usecase

import com.canerture.splash.domain.repository.SplashRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val splashRepository: SplashRepository,
) {
    suspend operator fun invoke() = splashRepository.isUserLoggedIn()
}