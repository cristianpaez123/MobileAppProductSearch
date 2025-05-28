package com.example.mobileappproductsearch.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected inline fun launch(
        scope: CoroutineScope = viewModelScope,
        crossinline onError: (Throwable) -> Unit = {},
        crossinline block: suspend () -> Unit
    ) {
        scope.launch {
            try {
                block()
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }
}