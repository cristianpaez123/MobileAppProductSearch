package com.example.mobileappproductsearch.data.repository

import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.domain.model.LoginResult
import com.example.mobileappproductsearch.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): LoginResult {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            LoginResult.Success
        } catch (e: Exception) {
            val resId = when (e) {
                is FirebaseAuthInvalidUserException -> R.string.error_user_not_found
                is FirebaseAuthInvalidCredentialsException -> R.string.error_invalid_credentials
                is FirebaseAuthUserCollisionException -> R.string.error_user_already_exists
                is FirebaseNetworkException -> R.string.error_network
                else -> R.string.error_unexpected
            }
            LoginResult.Failure(resId)
        }
    }

}