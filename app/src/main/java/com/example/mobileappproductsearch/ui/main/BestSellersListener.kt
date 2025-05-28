package com.example.mobileappproductsearch.ui.main

import com.example.mobileappproductsearch.ui.common.UiState
import com.example.mobileappproductsearch.ui.model.ProductUi

interface BestSellersListener {
    fun onProductSelected(product: ProductUi)
    fun showError(error: UiState.Error, retryAction: () -> Unit)
}
