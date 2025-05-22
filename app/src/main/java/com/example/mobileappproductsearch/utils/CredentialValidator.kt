package com.example.mobileappproductsearch.utils

import androidx.annotation.StringRes
import com.example.mobileappproductsearch.R

object CredentialValidator {

    private val EMAIL_PATTERN = Regex(
        "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
    )

    @StringRes
    fun validate(email: String?, password: String?): Int? {
        if (email.isNullOrBlank()) {
            return R.string.error_email_required
        }

        if (!EMAIL_PATTERN.matches(email)) {
            return R.string.error_email_invalid
        }

        if (password.isNullOrBlank()) {
            return R.string.error_password_required
        }

        return null
    }
}