package com.canerture.login.domain.usecase

import com.canerture.login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return loginRepository.login(email, password)
    }
}