package com.example.mobileappproductsearch.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappproductsearch.domain.useCase.LoginUseCase
import com.example.mobileappproductsearch.utils.CredentialValidator
import com.example.mobileappproductsearch.utils.FirebaseErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val firebaseErrorMapper: FirebaseErrorMapper
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
                loginUseCase(email, password)
                _uiState.value = LoginUiState.Success
            } catch (e: Exception) {
                val messageRes = firebaseErrorMapper.map(e)
                _uiState.value = LoginUiState.Error.MessageRes(messageRes)
            }
        }
    }

}