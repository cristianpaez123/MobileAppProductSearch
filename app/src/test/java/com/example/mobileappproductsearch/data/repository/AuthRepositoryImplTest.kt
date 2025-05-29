package com.example.mobileappproductsearch.data.repository

import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.domain.model.LoginResult
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

 private lateinit var repository: AuthRepositoryImpl
 private val firebaseAuth: FirebaseAuth = mock()

 private val email = "test@example.com"
 private val password = "123456"

 @Before
 fun setup() {
  repository = AuthRepositoryImpl(firebaseAuth)
 }

 @Test
 fun login_success_returnsSuccess() = runTest {
  // given
  val authResult: AuthResult = mock()
  whenever(firebaseAuth.signInWithEmailAndPassword(email, password))
   .thenReturn(Tasks.forResult(authResult))

  // when
  val result = repository.login(email, password)

  // then
  assertEquals(LoginResult.Success, result)
 }

 @Test
 fun login_invalidUser_returnsUserNotFoundError() = runTest {
  // given
  val exception = mock<FirebaseAuthInvalidUserException>()
  whenever(firebaseAuth.signInWithEmailAndPassword(email, password))
   .thenReturn(Tasks.forException(exception))

  // when
  val result = repository.login(email, password)

  // then
  assertEquals(LoginResult.Failure(R.string.error_user_not_found), result)
 }


 @Test
 fun login_invalidCredentials_returnsInvalidCredentialsError() = runTest {
  // given
  val exception = mock<FirebaseAuthInvalidCredentialsException>()
  whenever(firebaseAuth.signInWithEmailAndPassword(email, password))
   .thenReturn(Tasks.forException(exception))

  // when
  val result = repository.login(email, password)

  // then
  assertEquals(LoginResult.Failure(R.string.error_invalid_credentials), result)
 }

 @Test
 fun login_userCollision_returnsUserAlreadyExistsError() = runTest {
  // given
  val exception = mock<FirebaseAuthUserCollisionException>()
  whenever(firebaseAuth.signInWithEmailAndPassword(email, password))
   .thenReturn(Tasks.forException(exception))

  // when
  val result = repository.login(email, password)

  // then
  assertEquals(LoginResult.Failure(R.string.error_user_already_exists), result)
 }

 @Test
 fun login_networkException_returnsNetworkError() = runTest {
  // given
  val exception = mock<FirebaseNetworkException>()
  whenever(firebaseAuth.signInWithEmailAndPassword(email, password))
   .thenReturn(Tasks.forException(exception))

  // when
  val result = repository.login(email, password)

  // then
  assertEquals(LoginResult.Failure(R.string.error_network), result)
 }

 @Test
 fun login_unknownException_returnsUnexpectedError() = runTest {
  // given
  val exception = RuntimeException("Unknown error")
  whenever(firebaseAuth.signInWithEmailAndPassword(email, password))
   .thenReturn(Tasks.forException(exception))

  // when
  val result = repository.login(email, password)

  // then
  assertEquals(LoginResult.Failure(R.string.error_unexpected), result)
 }
}
