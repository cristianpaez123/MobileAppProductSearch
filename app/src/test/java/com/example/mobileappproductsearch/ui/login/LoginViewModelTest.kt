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
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

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
        // given
        whenever(loginUseCase("test@example.com", "123456"))
            .thenReturn(LoginResult.Success)

        // when
        viewModel.login("test@example.com", "123456")

        // then
        assert(viewModel.uiState.value is UiState.Loading)
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assert(state is UiState.Success)
    }

    @Test
    fun login_whenEmailIsInvalid_emitsError() = runTest {
        // when
        viewModel.login("invalid-email", "123456")

        // then
        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageRes)
        assert((state as UiState.Error.MessageRes).resId == R.string.error_email_invalid)
    }

    @Test
    fun login_whenPasswordIsEmpty_emitsError() = runTest {
        // when
        viewModel.login("test@example.com", "")

        // then
        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageRes)
        assert((state as UiState.Error.MessageRes).resId == R.string.error_password_required)
    }

    @Test
    fun login_whenEmailIsEmpty_emitsError() = runTest {
        // when
        viewModel.login("", "123456")

        // then
        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageRes)
        assert((state as UiState.Error.MessageRes).resId == R.string.error_email_required)
    }

    @Test
    fun login_useCaseThrowsException_emitsMessageText() = runTest {
        // given
        whenever(loginUseCase("test@example.com", "123456"))
            .thenThrow(RuntimeException("Network error"))

        // when
        viewModel.login("test@example.com", "123456")
        advanceUntilIdle()

        // then
        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageText)
        assert((state as UiState.Error.MessageText).message == "Network error")
    }

    @Test
    fun login_useCaseThrowsNullMessageException_emitsUnexpectedError() = runTest {
        // given
        whenever(loginUseCase("test@example.com", "123456"))
            .thenThrow(RuntimeException())

        // when
        viewModel.login("test@example.com", "123456")

        // then
        assert(viewModel.uiState.value is UiState.Loading)
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageRes)
        assert((state as UiState.Error.MessageRes).resId == R.string.error_unexpected)
    }
}
