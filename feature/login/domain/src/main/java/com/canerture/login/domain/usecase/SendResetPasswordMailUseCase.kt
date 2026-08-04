package com.canerture.login.domain.usecase

import com.canerture.login.domain.repository.LoginRepository
import javax.inject.Inject

class SendResetPasswordMailUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
) {
    suspend operator fun invoke(email: String): Result<String> {
        return loginRepository.sendResetPasswordMail(email)
    }
}