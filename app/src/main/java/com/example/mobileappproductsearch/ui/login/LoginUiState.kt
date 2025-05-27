package com.example.mobileappproductsearch.ui.login

import androidx.annotation.StringRes

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    sealed class Error : LoginUiState() {
        data class MessageRes(@StringRes val resId: Int) : Error()
        data class MessageText(val message: String) : Error()
        data object Unknown : Error()
    }
}