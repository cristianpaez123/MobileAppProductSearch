package com.example.mobileappproductsearch.ui.login

import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.domain.model.LoginResult
import com.example.mobileappproductsearch.domain.useCase.LoginUseCase
import com.example.mobileappproductsearch.ui.common.BaseViewModel
import com.example.mobileappproductsearch.utils.CredentialValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (!isInputValid(email, password)) return

        _uiState.value = LoginUiState.Loading

        launch(onError = ::handleLoginError) {
            val result = loginUseCase(email, password)
            handleLoginResult(result)
        }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        val errorResId = CredentialValidator.validate(email, password)
        return if (errorResId != null) {
            _uiState.value = LoginUiState.Error.MessageRes(errorResId)
            false
        } else {
            true
        }
    }

    private fun handleLoginResult(result: LoginResult) {
        _uiState.value = when (result) {
            is LoginResult.Success -> LoginUiState.Success
            is LoginResult.Failure -> LoginUiState.Error.MessageRes(result.errorResId)
        }
    }

    private fun handleLoginError(throwable: Throwable) {
        val messageUiState = throwable.message?.let {
            LoginUiState.Error.MessageText(it)
        } ?: LoginUiState.Error.MessageRes(R.string.error_unexpected)

        _uiState.value = messageUiState
    }
}
