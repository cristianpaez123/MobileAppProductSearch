package com.example.mobileappproductsearch.data.repository
import com.example.mobileappproductsearch.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResult =
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
}