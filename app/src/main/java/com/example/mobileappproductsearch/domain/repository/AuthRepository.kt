package com.example.mobileappproductsearch.domain.repository

import com.example.mobileappproductsearch.domain.model.LoginResult

interface AuthRepository {
    suspend fun login(email: String, password: String): LoginResult
}