package com.example.mobileappproductsearch.ui.login

import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.domain.model.LoginResult
import com.example.mobileappproductsearch.domain.useCase.LoginUseCase
import com.example.mobileappproductsearch.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelMockitoTest {

    private lateinit var viewModel: LoginViewModel
    private val loginUseCase: LoginUseCase = mock()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun login_success() = runTest {
        whenever(loginUseCase("test@example.com", "123456"))
            .thenReturn(LoginResult.Success)

        viewModel.login("test@example.com", "123456")

        assert(viewModel.uiState.value is UiState.Loading)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is UiState.Success)
    }

    @Test
    fun login_whenEmailIsInvalid_emitsError() = runTest {

        viewModel.login("invalid-email", "123456")

        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageRes)
        assert((state as UiState.Error.MessageRes).resId == R.string.error_email_invalid)
    }

    @Test
    fun login_whenPasswordIsEmpty_emitsError() = runTest {

        viewModel.login("test@example.com", "")

        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageRes)
        assert((state as UiState.Error.MessageRes).resId == R.string.error_password_required)
    }

    @Test
    fun login_whenEmailIsEmpty_emitsError() = runTest {

        viewModel.login("", "123456")

        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageRes)
        assert((state as UiState.Error.MessageRes).resId == R.string.error_email_required)
    }

    @Test
    fun login_useCaseThrowsException_emitsMessageText() = runTest {
        whenever(loginUseCase("test@example.com", "123456"))
            .thenThrow(RuntimeException("Network error"))

        viewModel.login("test@example.com", "123456")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageText)
        assert((state as UiState.Error.MessageText).message == "Network error")
    }

    @Test
    fun login_useCaseThrowsNullMessageException_emitsUnexpectedError() = runTest {
        whenever(loginUseCase("test@example.com", "123456"))
            .thenThrow(RuntimeException())

        viewModel.login("test@example.com", "123456")

        assert(viewModel.uiState.value is UiState.Loading)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageRes)
        assert((state as UiState.Error.MessageRes).resId == R.string.error_unexpected)
    }

}
