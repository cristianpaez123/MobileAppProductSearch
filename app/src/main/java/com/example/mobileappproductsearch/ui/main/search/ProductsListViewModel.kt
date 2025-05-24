package com.example.mobileappproductsearch.ui.main.search

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappproductsearch.domain.repository.useCase.LoginUseCase
import com.example.mobileappproductsearch.domain.repository.useCase.ProductsListUseCase
import com.example.mobileappproductsearch.ui.login.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val productsListUseCase: ProductsListUseCase,

    ) : ViewModel() {
    private val _uiState = MutableLiveData<SearchResultUiState>()
    val uiState: LiveData<SearchResultUiState> get() = _uiState


    fun searchProduct(keyword: String) {
        viewModelScope.launch {
            try {
                productsListUseCase(keyword)
                _uiState.value = SearchResultUiState.Success
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    401 -> "No autorizado. Verifica tu token."
                    404 -> "No se encontraron productos."
                   else -> "Error de servidor (${e.code()})"
                }
                _uiState.value = SearchResultUiState.Error(message = errorMessage)
            } catch (e: IOException) {
                _uiState.value = SearchResultUiState.Error(message = "Error de red. Verifica tu conexión.")
            } catch (e: Exception) {
                _uiState.value = SearchResultUiState.Error(message = e.localizedMessage ?: "Error desconocido")
            }
        }

    }

    sealed class SearchResultUiState {

        object Loading : SearchResultUiState()

        object Success : SearchResultUiState()

        data class Error(
            @StringRes val messageRes: Int? = null,
            val message: String? = null
        ) : SearchResultUiState()
    }
}