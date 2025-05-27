package com.example.mobileappproductsearch.domain.model

import androidx.annotation.StringRes

sealed class LoginResult {
    data object Success : LoginResult()
    data class Failure(@StringRes val errorResId: Int) : LoginResult()
}
