package com.canerture.welcome.domain.usecase

import com.canerture.welcome.domain.repository.WelcomeRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val welcomeRepository: WelcomeRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return welcomeRepository.loginWithGoogle()
    }
}