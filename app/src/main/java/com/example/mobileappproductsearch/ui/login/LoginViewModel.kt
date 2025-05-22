package com.example.mobileappproductsearch.ui.login

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappproductsearch.domain.repository.useCase.LoginUseCase
import com.example.mobileappproductsearch.utils.CredentialValidator
import com.example.mobileappproductsearch.utils.FirebaseErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<LoginUiState>()
    val uiState: LiveData<LoginUiState> get() = _uiState


    fun login(email: String, password: String) {
        val errorResId = CredentialValidator.validate(email, password)
        if (errorResId != null) {
            _uiState.value = LoginUiState.Error(errorResId)
            return
        }
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            try {
                loginUseCase(email, password)
                _uiState.value = LoginUiState.Success
            } catch (e: Exception) {
                val messageRes = FirebaseErrorMapper.map(e)
                _uiState.value = LoginUiState.Error(messageRes)
            }
        }
    }

    sealed class LoginUiState {

        object Loading : LoginUiState()

        object Success : LoginUiState()

        data class Error(
            @StringRes
            val messageRes: Int? = null,
            val message: String? = null
        ) : LoginUiState()
    }

}