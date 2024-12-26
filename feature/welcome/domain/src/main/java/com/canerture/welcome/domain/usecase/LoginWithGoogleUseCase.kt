package com.canerture.welcome.domain.usecase

import com.canerture.core.common.Resource
import com.canerture.welcome.domain.repository.WelcomeRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val welcomeRepository: WelcomeRepository,
) {
    suspend operator fun invoke(): Resource<Unit> {
        return welcomeRepository.loginWithGoogle()
    }
}