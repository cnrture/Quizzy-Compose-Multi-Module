package com.canerture.register.domain.usecase

import com.canerture.core.common.Resource
import com.canerture.register.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val registerRepository: RegisterRepository,
) {
    suspend operator fun invoke(email: String, username: String, password: String): Resource<Unit> {
        return registerRepository.register(email, username, password)
    }
}