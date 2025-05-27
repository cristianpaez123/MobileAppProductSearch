package com.example.mobileappproductsearch.ui.main.search

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappproductsearch.domain.repository.useCase.ProductsListUseCase
import com.example.mobileappproductsearch.domain.useCase.CategoriesUseCase
import com.example.mobileappproductsearch.domain.useCase.getProductsByCategoryUseCase
import com.example.mobileappproductsearch.ui.model.CategoryModelUi
import com.example.mobileappproductsearch.ui.model.ProductUi
import com.example.mobileappproductsearch.ui.model.toUi
import com.example.mobileappproductsearch.utils.ProductSearchErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val productsListUseCase: ProductsListUseCase,
    private val categoriesUseCase: CategoriesUseCase,
    private val getProductsByCategoryUseCase: getProductsByCategoryUseCase,
    private val productSearchErrorMapper: ProductSearchErrorMapper

) : ViewModel() {
    private val _uiState = MutableLiveData<SearchResultUiState>()
    val uiState: LiveData<SearchResultUiState> get() = _uiState

    private val _suggestions = MutableLiveData<List<ProductUi>>()
    val suggestions: LiveData<List<ProductUi>> get() = _suggestions

    private val _uiCategories = MutableLiveData<List<CategoryModelUi>>()
    val uiCategories: LiveData<List<CategoryModelUi>> get() = _uiCategories

    private val _bestSellers = MutableLiveData<List<ProductUi>>()
    val bestSellers: LiveData<List<ProductUi>> get() = _bestSellers


    fun searchProduct(keyword: String) {
        _uiState.value = SearchResultUiState.Loading
        viewModelScope.launch {
            try {
                val products = productsListUseCase(keyword)
                _uiState.value = SearchResultUiState.Success(products.map { it.toUi() })
                fetchCategories(keyword)
            } catch (e: HttpException) {
                val messageRes = productSearchErrorMapper.mapError(e)
                _uiState.value = SearchResultUiState.Error(messageRes)
            }
        }
    }

    private fun fetchCategories(keyword: String) {
        viewModelScope.launch {
            try {
                val categories = categoriesUseCase(keyword)
                _uiCategories.value = categories.map { it.toUi() }
            } catch (e: Exception) {
                _uiCategories.value = emptyList()
            }
        }
    }

    fun getSuggestions(keyword: String) {
        viewModelScope.launch {
            try {
                val products = productsListUseCase(keyword)
                _suggestions.value = products.map { it.toUi() }
            } catch (e: Exception) {
                _suggestions.value = emptyList()
            }
        }
    }

    fun searchProductByCategory(keyword: String,category:String) {
        viewModelScope.launch {
            try {
                val products = getProductsByCategoryUseCase(keyword, category)
                _uiState.value = SearchResultUiState.Success(products.map { it.toUi() })
            } catch (e: Exception) {
                _suggestions.value = emptyList()
            }
        }
    }

    fun getBestSellers(keyword: String) {
        viewModelScope.launch {
            try {
                val products = productsListUseCase(keyword)
                _bestSellers.value = products.map { it.toUi() }
            } catch (e: HttpException) {
                _suggestions.value = emptyList()
            }
        }
    }

    sealed class SearchResultUiState {

        object Loading : SearchResultUiState()

        data class Success(val products: List<ProductUi>) : SearchResultUiState()

        data class Error(
            @StringRes val messageRes: Int? = null,
            val message: String? = null
        ) : SearchResultUiState()
    }
}