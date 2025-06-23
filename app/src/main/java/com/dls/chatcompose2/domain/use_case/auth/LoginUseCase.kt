package com.dls.chatcompose2.domain.use_case.auth

import com.dls.chatcompose2.domain.repository.AuthRepository
import javax.inject.Inject


class LoginUseCase @Inject constructor(repository: AuthRepository) {
    operator fun invoke(email: String, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}