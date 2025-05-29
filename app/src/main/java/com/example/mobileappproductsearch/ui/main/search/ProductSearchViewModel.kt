package com.example.mobileappproductsearch.ui.main.search

import com.example.mobileappproductsearch.domain.useCase.CategoriesUseCase
import com.example.mobileappproductsearch.domain.useCase.SearchProductsByCategoryUseCase
import com.example.mobileappproductsearch.domain.useCase.SearchProductsUseCase
import com.example.mobileappproductsearch.ui.common.BaseViewModel
import com.example.mobileappproductsearch.ui.common.UiState
import com.example.mobileappproductsearch.ui.model.CategoryModelUi
import com.example.mobileappproductsearch.ui.model.ProductUi
import com.example.mobileappproductsearch.ui.model.toUi
import com.example.mobileappproductsearch.utils.ProductSearchErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    private val categoriesUseCase: CategoriesUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val searchProductsByCategoryUseCase: SearchProductsByCategoryUseCase,
    private val productSearchErrorMapper: ProductSearchErrorMapper
) : BaseViewModel() {

    private val _searchProductUiState = MutableStateFlow<UiState<List<ProductUi>>>(UiState.Initial)
    val searchProductUiState: StateFlow<UiState<List<ProductUi>>> = _searchProductUiState

    private val _categories = MutableStateFlow<List<CategoryModelUi>>(emptyList())
    val categories: StateFlow<List<CategoryModelUi>> = _categories

    fun searchProduct(keyword: String) {
        _searchProductUiState.value = UiState.Loading
        launch(onError = {
            _searchProductUiState.value =
                UiState.Error.MessageRes(productSearchErrorMapper.mapError(it))
        }) {
            val products = searchProductsUseCase(keyword)
            _searchProductUiState.value = if (products.isNotEmpty()) {
                UiState.Success(products.map { it.toUi() })
            } else {
                UiState.EmptyData
            }
        }
        fetchCategories(keyword)
    }

    fun searchProductByCategory(keyword: String, category: String) {
        _searchProductUiState.value = UiState.Loading
        launch(onError = {
            _searchProductUiState.value =
                UiState.Error.MessageRes(productSearchErrorMapper.mapError(it))
        }) {
            val products = searchProductsByCategoryUseCase(keyword, category)
            _searchProductUiState.value = UiState.Success(products.map { it.toUi() })
        }
    }

    private fun fetchCategories(keyword: String) {
        launch(onError = { _categories.value = emptyList() }) {
            val categories = categoriesUseCase(keyword)
            _categories.value = categories.map { it.toUi() }
        }
    }

    fun onBackPressed() {
        _searchProductUiState.value = UiState.Initial
    }
}