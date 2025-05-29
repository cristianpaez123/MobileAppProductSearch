package com.example.mobileappproductsearch.ui.common

import androidx.annotation.StringRes

sealed class UiState<out T> {

    data object Initial : UiState<Nothing>()

    data object Loading : UiState<Nothing>()

    data class Success<T>(val data: T) : UiState<T>()

    sealed class Error : UiState<Nothing>() {
        data class MessageRes(@StringRes val resId: Int) : Error()
        data class MessageText(val message: String) : Error()
    }
}
