package com.canerture.login.domain.usecase

import com.canerture.core.common.Resource
import com.canerture.login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
) {
    suspend operator fun invoke(email: String, password: String): Resource<Unit> {
        return loginRepository.login(email, password)
    }
}