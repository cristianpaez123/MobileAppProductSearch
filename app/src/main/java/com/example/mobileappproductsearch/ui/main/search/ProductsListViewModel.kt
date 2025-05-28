package com.example.mobileappproductsearch.ui.main.search

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobileappproductsearch.domain.useCase.CategoriesUseCase
import com.example.mobileappproductsearch.domain.useCase.SearchProductsByCategoryUseCase
import com.example.mobileappproductsearch.domain.useCase.SearchProductsUseCase
import com.example.mobileappproductsearch.ui.common.BaseViewModel
import com.example.mobileappproductsearch.ui.model.CategoryModelUi
import com.example.mobileappproductsearch.ui.model.ProductUi
import com.example.mobileappproductsearch.ui.model.toUi
import com.example.mobileappproductsearch.utils.ProductSearchErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val categoriesUseCase: CategoriesUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val searchProductsByCategoryUseCase: SearchProductsByCategoryUseCase,
    private val productSearchErrorMapper: ProductSearchErrorMapper
) : BaseViewModel() {

    private val _uiState = MutableLiveData<SearchResultUiState>()
    val uiState: LiveData<SearchResultUiState> = _uiState

    private val _suggestions = MutableLiveData<List<ProductUi>>()
    val suggestions: LiveData<List<ProductUi>> = _suggestions

    private val _uiCategories = MutableLiveData<List<CategoryModelUi>>()
    val uiCategories: LiveData<List<CategoryModelUi>> = _uiCategories

    private val _bestSellers = MutableLiveData<List<ProductUi>>()
    val bestSellers: LiveData<List<ProductUi>> = _bestSellers

    fun searchProduct(keyword: String) {
        _uiState.value = SearchResultUiState.Loading
        launch(
            onError = { e ->
                _uiState.value = SearchResultUiState.Error(
                    messageRes = productSearchErrorMapper.mapError(e)
                )
            }
        ) {
            val products = searchProductsUseCase(keyword)
            _uiState.value = SearchResultUiState.Success(products.map { it.toUi() })
            fetchCategories(keyword)
        }
    }

    fun searchProductByCategory(keyword: String, category: String) {
        _uiState.value = SearchResultUiState.Loading
        launch (
            onError = {
                _uiState.value = SearchResultUiState.Error()
            }
        ) {
            val products = searchProductsByCategoryUseCase(keyword, category)
            _uiState.value = SearchResultUiState.Success(products.map { it.toUi() })
        }
    }

    fun loadSuggestions(keyword: String) {
        launch(
            onError = { _suggestions.value = emptyList() }
        ) {
            val products = searchProductsUseCase(keyword)
            _suggestions.value = products.map { it.toUi() }
        }
    }

    fun loadBestSellers(keyword: String) {
        launch(
            onError = { _bestSellers.value = emptyList() }
        ) {
            val products = searchProductsUseCase(keyword)
            _bestSellers.value = products.map { it.toUi() }
        }
    }

    private fun fetchCategories(keyword: String) {
        launch(
            onError = { _uiCategories.value = emptyList() }
        ) {
            val categories = categoriesUseCase(keyword)
            _uiCategories.value = categories.map { it.toUi() }
        }
    }

    sealed class SearchResultUiState {
        data object Loading : SearchResultUiState()
        data class Success(val products: List<ProductUi>) : SearchResultUiState()
        data class Error(
            @StringRes val messageRes: Int? = null,
            val message: String? = null
        ) : SearchResultUiState()
    }
}
