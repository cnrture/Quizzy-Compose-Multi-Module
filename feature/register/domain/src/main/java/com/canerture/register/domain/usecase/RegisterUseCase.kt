package com.canerture.register.domain.usecase

import com.canerture.register.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val registerRepository: RegisterRepository,
) {
    suspend operator fun invoke(email: String, username: String, password: String): Result<String> {
        return registerRepository.register(email, username, password)
    }
}