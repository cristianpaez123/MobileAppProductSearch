package com.example.mobileappproductsearch.domain.useCase

import com.example.mobileappproductsearch.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult = authRepository.login(email, password)
}