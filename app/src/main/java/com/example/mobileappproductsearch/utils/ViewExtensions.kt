package com.example.mobileappproductsearch.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.example.mobileappproductsearch.R

fun Context.resolveRowColor(isGray: Boolean): Int {
    return ContextCompat.getColor(this, if (isGray) R.color.colorTertiary else R.color.colorSurface)
}

fun View.visible(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}
