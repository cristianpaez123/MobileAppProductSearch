package com.example.mobileappproductsearch.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.example.mobileappproductsearch.R

fun Context.resolveRowColor(isGray: Boolean): Int {
    return if (isGray) {
        // todo: fix
//        ContextCompat.getColor(this, Color.GRAY)
        Color.GRAY
    } else {
        Color.WHITE
    }
}

fun View.visible(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}
