package com.example.mobileappproductsearch.ui.main.bestSellers

import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.domain.model.Product
import com.example.mobileappproductsearch.domain.useCase.SearchProductsUseCase
import com.example.mobileappproductsearch.ui.common.UiState
import com.example.mobileappproductsearch.ui.model.ProductUi
import com.example.mobileappproductsearch.ui.model.toUi
import com.example.mobileappproductsearch.utils.ProductSearchErrorMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class BestSellersViewModelTest {

    private lateinit var viewModel: BestSellersViewModel
    private val searchProductsUseCase: SearchProductsUseCase = mock()
    private val errorMapper: ProductSearchErrorMapper = mock()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = BestSellersViewModel(searchProductsUseCase, errorMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadBestSellers_success_emitsLoadingThenSuccess() = runTest {
        // given
        val expectedUi = product1.toUi()

        whenever(searchProductsUseCase("los mas vendidos")).thenReturn(listOf(product1))

        // when
        viewModel.loadBestSellers()

        // then
        assert(viewModel.uiState.value is UiState.Loading)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is UiState.Success)
        assert((state as UiState.Success).data == listOf(expectedUi))
    }

    @Test
    fun loadBestSellers_failure_emitsLoadingThenMappedError() = runTest {
        // given
        val exception = RuntimeException("some failure")
        val expectedErrorResId = R.string.error_unexpected

        whenever(searchProductsUseCase("los mas vendidos")).thenThrow(exception)
        whenever(errorMapper.mapError(exception)).thenReturn(expectedErrorResId)

        // when
        viewModel.loadBestSellers()

        // then
        assert(viewModel.uiState.value is UiState.Loading)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is UiState.Error.MessageRes)
        assert((state as UiState.Error.MessageRes).resId == expectedErrorResId)
    }

    private val product1 = Product(
        id = "1",
        name = "Product 1",
        status = "active",
        attributes = emptyList(),
        pictures = emptyList(),
        description = "Test product"
    )

}
