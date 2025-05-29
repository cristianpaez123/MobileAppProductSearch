package com.example.mobileappproductsearch.ui.main.search

import com.example.mobileappproductsearch.domain.useCase.SearchProductsUseCase
import com.example.mobileappproductsearch.ui.common.BaseViewModel
import com.example.mobileappproductsearch.ui.model.ProductUi
import com.example.mobileappproductsearch.ui.model.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SuggestionsViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
) : BaseViewModel() {

    private val _suggestions = MutableStateFlow<List<ProductUi>>(emptyList())
    val suggestions: StateFlow<List<ProductUi>> = _suggestions

    fun loadSuggestions(keyword: String) {
        launch(onError = { _suggestions.value = emptyList() }) {
            val products = searchProductsUseCase(keyword)
            _suggestions.value = products.map { it.toUi() }
        }
    }
}
