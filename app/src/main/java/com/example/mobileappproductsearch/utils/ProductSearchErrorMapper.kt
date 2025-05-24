package com.example.mobileappproductsearch.utils

import androidx.annotation.StringRes
import com.example.mobileappproductsearch.R
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProductSearchErrorMapper @Inject constructor() {
    @StringRes
    fun mapError(throwable: Throwable): Int {
        return when (throwable) {
            is HttpException -> when (throwable.code()) {
                401 -> R.string.error_unauthorized
                404 -> R.string.error_not_found
                else -> R.string.error_server_generic
            }
            is IOException -> R.string.error_network
            else -> R.string.error_unexpected
        }
    }
}
