package com.example.mobileappproductsearch.ui.main.search

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class ProductsListViewModel @Inject constructor(
    private val categoriesUseCase: CategoriesUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val searchProductsByCategoryUseCase: SearchProductsByCategoryUseCase,
    private val productSearchErrorMapper: ProductSearchErrorMapper
) : BaseViewModel() {

    private val _bestSellersUiState = MutableStateFlow<UiState<List<ProductUi>>>(UiState.Idle)
    val bestSellersUiState: StateFlow<UiState<List<ProductUi>>> = _bestSellersUiState
    
    private val _searchProductUiState = MutableStateFlow<UiState<List<ProductUi>>>(UiState.Idle)
    val searchProductUiState: StateFlow<UiState<List<ProductUi>>> = _searchProductUiState

    private val _suggestions = MutableLiveData<List<ProductUi>>()
    val suggestions: LiveData<List<ProductUi>> = _suggestions

    private val _uiCategories = MutableLiveData<List<CategoryModelUi>>()
    val uiCategories: LiveData<List<CategoryModelUi>> = _uiCategories

    fun loadBestSellers() {
        _bestSellersUiState.value = UiState.Loading
        launch(
            onError = { error ->
                _bestSellersUiState.value =
                    UiState.Error.MessageRes(productSearchErrorMapper.mapError(error))
            }
        ) {
            val products = searchProductsUseCase(BEST_SELLER)
            _bestSellersUiState.value = UiState.Success(products.map { it.toUi() })
        }
    }
    
    fun searchProduct(keyword: String) {
        _searchProductUiState.value = UiState.Loading
        launch(
            onError = { error ->
                _searchProductUiState.value =
                    UiState.Error.MessageRes(productSearchErrorMapper.mapError(error))
            }
        ) {
            val products = searchProductsUseCase(keyword)
            _searchProductUiState.value = UiState.Success(products.map { it.toUi() })
            fetchCategories(keyword)
        }
    }

    fun searchProductByCategory(keyword: String, category: String) {
        _searchProductUiState.value = UiState.Loading
        launch (
            onError = { error ->
                _searchProductUiState.value =
                    UiState.Error.MessageRes(productSearchErrorMapper.mapError(error))
            }
        ) {
            val products = searchProductsByCategoryUseCase(keyword, category)
            _searchProductUiState.value = UiState.Success(products.map { it.toUi() })
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

    private companion object {
        const val BEST_SELLER = "los mas vendidos"
    }
}
