package com.example.mobileappproductsearch.domain.repository

import com.google.firebase.auth.AuthResult

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
}