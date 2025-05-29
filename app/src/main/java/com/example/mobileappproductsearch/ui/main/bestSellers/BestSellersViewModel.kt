package com.example.mobileappproductsearch.ui.main.bestSellers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappproductsearch.domain.useCase.SearchProductsUseCase
import com.example.mobileappproductsearch.ui.common.UiState
import com.example.mobileappproductsearch.ui.model.ProductUi
import com.example.mobileappproductsearch.ui.model.toUi
import com.example.mobileappproductsearch.utils.ProductSearchErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BestSellersViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val errorMapper: ProductSearchErrorMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ProductUi>>>(UiState.Initial)
    val uiState: StateFlow<UiState<List<ProductUi>>> = _uiState

    fun loadBestSellers() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val products = searchProductsUseCase(BEST_SELLER)
                _uiState.value = UiState.Success(products.map { it.toUi() })
            } catch (e: Exception) {
                _uiState.value = UiState.Error.MessageRes(errorMapper.mapError(e))
            }
        }
    }

    private companion object {
        const val BEST_SELLER = "los mas vendidos"
    }
}