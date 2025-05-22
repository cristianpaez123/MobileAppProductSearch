package com.example.mobileappproductsearch.utils

import androidx.annotation.StringRes
import com.example.mobileappproductsearch.R
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class FirebaseErrorMapper {
    companion object {
        @StringRes
        fun map(throwable: Throwable): Int {
            return when (throwable) {
                is FirebaseAuthInvalidUserException -> R.string.error_user_not_found
                is FirebaseAuthInvalidCredentialsException -> R.string.error_invalid_credentials
                is FirebaseAuthUserCollisionException -> R.string.error_user_already_exists
                is FirebaseNetworkException -> R.string.error_network
                else -> R.string.error_unexpected
            }
        }
    }
}