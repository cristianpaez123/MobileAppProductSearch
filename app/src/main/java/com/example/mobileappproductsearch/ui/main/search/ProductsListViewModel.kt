package com.example.mobileappproductsearch.ui.main.search

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappproductsearch.domain.repository.useCase.ProductsListUseCase
import com.example.mobileappproductsearch.ui.model.ProductModelUi
import com.example.mobileappproductsearch.ui.model.toUiModel
import com.example.mobileappproductsearch.utils.ProductSearchErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlinx.coroutines.delay


@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val productsListUseCase: ProductsListUseCase,
    private val productSearchErrorMapper: ProductSearchErrorMapper

) : ViewModel() {
    private val _uiState = MutableLiveData<SearchResultUiState>()
    val uiState: LiveData<SearchResultUiState> get() = _uiState

    private val _suggestions = MutableLiveData<List<ProductModelUi>>()
    val suggestions: LiveData<List<ProductModelUi>> get()  = _suggestions

    private var suggestionJob: Job? = null


    fun searchProduct(keyword: String) {
        _uiState.value = SearchResultUiState.Loading
        viewModelScope.launch {
            try {
                val products = productsListUseCase(keyword)
                val uiModels = products.map { it.toUiModel() }
                _uiState.value = SearchResultUiState.Success(uiModels)
            } catch (e: HttpException) {
                val messageRes = productSearchErrorMapper.mapError(e)
                _uiState.value = SearchResultUiState.Error(messageRes)
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

    fun getSuggestions(query: String) {
        viewModelScope.launch {
            try {
                val allProducts = productsListUseCase(query)
                _suggestions.value = allProducts.map { it.toUiModel() }
            } catch (e: Exception) {
                _suggestions.value = emptyList()
            }
        }
    }
}