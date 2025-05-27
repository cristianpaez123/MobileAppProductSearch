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
import com.example.mobileappproductsearch.ui.model.ProductModelUi
import com.example.mobileappproductsearch.ui.model.toUiModel
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

    private val _suggestions = MutableLiveData<List<ProductModelUi>>()
    val suggestions: LiveData<List<ProductModelUi>> get() = _suggestions

    private val _uiCategories = MutableLiveData<List<CategoryModelUi>>()
    val uiCategories: LiveData<List<CategoryModelUi>> get() = _uiCategories

    private val _bestSellers = MutableLiveData<List<ProductModelUi>>()
    val bestSellers: LiveData<List<ProductModelUi>> get() = _bestSellers


    fun searchProduct(keyword: String) {
        _uiState.value = SearchResultUiState.Loading
        viewModelScope.launch {
            try {
                val products = productsListUseCase(keyword)
                val uiModels = products.map { it.toUiModel() }
                _uiState.value = SearchResultUiState.Success(uiModels)
                searchCategories(keyword)
            } catch (e: HttpException) {
                val messageRes = productSearchErrorMapper.mapError(e)
                _uiState.value = SearchResultUiState.Error(messageRes)
            }
        }

    }

    fun searchCategories(keyword: String) {
        viewModelScope.launch {
            try {
                val products = categoriesUseCase(keyword)
                _uiCategories.value = products.map { it.toUiModel() }
            } catch (e: HttpException) {
                _uiCategories.value = emptyList()
            }
        }
    }

    fun getSuggestions(keyword: String) {
        viewModelScope.launch {
            try {
                val allProducts = productsListUseCase(keyword)
                _suggestions.value = allProducts.map { it.toUiModel() }
            } catch (e: Exception) {
                _suggestions.value = emptyList()
            }
        }
    }

    fun searchProductByCategory(keyword: String,category:String) {
        viewModelScope.launch {
            try {
                val products = getProductsByCategoryUseCase(keyword,category)
                val uiModels = products.map { it.toUiModel() }
                _uiState.value = SearchResultUiState.Success(uiModels)
            } catch (e: Exception) {
                _suggestions.value = emptyList()
            }
        }
    }

    fun getBestSellers(keyword: String) {
        viewModelScope.launch {
            try {
                val products = productsListUseCase(keyword)
                val uiModels = products.map { it.toUiModel() }
                _bestSellers.value = uiModels
            } catch (e: HttpException) {
                _suggestions.value = emptyList()
            }
        }
    }

    sealed class SearchResultUiState {

        object Loading : SearchResultUiState()

        data class Success(val products: List<ProductModelUi>) : SearchResultUiState()

        data class Error(
            @StringRes val messageRes: Int? = null,
            val message: String? = null
        ) : SearchResultUiState()
    }
}