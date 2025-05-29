package com.example.mobileappproductsearch.domain.useCase

import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.domain.model.LoginResult
import com.example.mobileappproductsearch.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUseCaseTest {

    private lateinit var useCase: LoginUseCase
    private val authRepository: AuthRepository = mock()

    @Before
    fun setup() {
        useCase = LoginUseCase(authRepository)
    }

    @Test
    fun invoke_returnsSuccessFromRepository() = runTest {
        // given
        val email = "test@example.com"
        val password = "123456"
        whenever(authRepository.login(email, password)).thenReturn(LoginResult.Success)

        // when
        val result = useCase(email, password)

        // then
        verify(authRepository).login(email, password)
        assertEquals(LoginResult.Success, result)
    }

    @Test
    fun invoke_returnsFailureFromRepository() = runTest {
        // given
        val email = "test@example.com"
        val password = "wrongpass"
        val failureResult = LoginResult.Failure(R.string.error_invalid_credentials)
        whenever(authRepository.login(email, password)).thenReturn(failureResult)

        // when
        val result = useCase(email, password)

        // then
        verify(authRepository).login(email, password)
        assertEquals(failureResult, result)
    }

    @Test
    fun invoke_repositoryThrows_exceptionPropagates() = runTest {
        // given
        val email = "test@example.com"
        val password = "exception"
        val exception = RuntimeException("network error")
        whenever(authRepository.login(email, password)).thenThrow(exception)

        // when + then
        val thrown = assertFailsWith<RuntimeException> {
            useCase(email, password)
        }

        assertEquals("network error", thrown.message)
        verify(authRepository).login(email, password)
    }
}
