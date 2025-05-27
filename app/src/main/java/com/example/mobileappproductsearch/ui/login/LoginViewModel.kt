package com.example.mobileappproductsearch.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.domain.model.LoginResult
import com.example.mobileappproductsearch.domain.useCase.LoginUseCase
import com.example.mobileappproductsearch.utils.CredentialValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun login(email: String, password: String) {
        val errorResId = CredentialValidator.validate(email, password)
        if (errorResId != null) {
            _uiState.value = LoginUiState.Error.MessageRes(resId = errorResId)
            return
        }
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            try {
                when (val result = loginUseCase(email, password)) {
                    is LoginResult.Success -> _uiState.value = LoginUiState.Success

                    is LoginResult.Failure ->
                        _uiState.value = LoginUiState.Error.MessageRes(result.errorResId)

                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error.MessageRes(R.string.error_unexpected)
            }
        }
    }

}