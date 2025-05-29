package com.example.mobileappproductsearch.ui.main.search

import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.domain.model.Category
import com.example.mobileappproductsearch.domain.model.Product
import com.example.mobileappproductsearch.domain.useCase.CategoriesUseCase
import com.example.mobileappproductsearch.domain.useCase.SearchProductsByCategoryUseCase
import com.example.mobileappproductsearch.domain.useCase.SearchProductsUseCase
import com.example.mobileappproductsearch.ui.common.UiState
import com.example.mobileappproductsearch.ui.model.CategoryModelUi
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProductSearchViewModelTest {

 private lateinit var viewModel: ProductSearchViewModel
 private val categoriesUseCase: CategoriesUseCase = mock()
 private val searchProductsUseCase: SearchProductsUseCase = mock()
 private val searchProductsByCategoryUseCase: SearchProductsByCategoryUseCase = mock()
 private val errorMapper: ProductSearchErrorMapper = mock()
 private val testDispatcher = StandardTestDispatcher()

 private val product1 = Product(
  id = "1",
  name = "Product 1",
  status = "active",
  attributes = listOf(),
  pictures = listOf(),
  description = "Test product"
 )

 private val category1 = Category(
  domainId = "cat1",
  domainName = "Category 1"
 )

 @Before
 fun setup() {
  Dispatchers.setMain(testDispatcher)
  viewModel = ProductSearchViewModel(
   categoriesUseCase,
   searchProductsUseCase,
   searchProductsByCategoryUseCase,
   errorMapper
  )
 }

 @After
 fun tearDown() {
  Dispatchers.resetMain()
 }

 @Test
 fun searchProduct_success_emitsLoadingThenSuccess_andUpdatesCategories() = runTest {
  // given
  val expectedUi = product1.toUi()
  val expectedCategoryUi = category1.toUi()

  whenever(searchProductsUseCase("zapatos")).thenReturn(listOf(product1))
  whenever(categoriesUseCase("zapatos")).thenReturn(listOf(category1))

  // when
  viewModel.searchProduct("zapatos")

  // then
  assertTrue(viewModel.searchProductUiState.value is UiState.Loading)
  advanceUntilIdle()

  assertEquals(UiState.Success(listOf(expectedUi)), viewModel.searchProductUiState.value)
  assertEquals(listOf(expectedCategoryUi), viewModel.categories.value)
 }

 @Test
 fun searchProduct_empty_emitsEmptyData() = runTest {
  // given
  whenever(searchProductsUseCase("zapatos")).thenReturn(emptyList())
  whenever(categoriesUseCase("zapatos")).thenReturn(emptyList())

  // when
  viewModel.searchProduct("zapatos")

  // then
  advanceUntilIdle()
  assertEquals(UiState.EmptyData, viewModel.searchProductUiState.value)
  assertEquals(emptyList<CategoryModelUi>(), viewModel.categories.value)
 }

 @Test
 fun searchProduct_error_emitsErrorState() = runTest {
  // given
  val exception = RuntimeException("error")
  whenever(searchProductsUseCase("zapatos")).thenThrow(exception)
  whenever(errorMapper.mapError(exception)).thenReturn(R.string.error_unexpected)

  // when
  viewModel.searchProduct("zapatos")

  // then
  advanceUntilIdle()
  val state = viewModel.searchProductUiState.value
  assertTrue(state is UiState.Error.MessageRes)
  assertEquals(R.string.error_unexpected, (state as UiState.Error.MessageRes).resId)
 }

 @Test
 fun searchProductByCategory_success_emitsSuccess() = runTest {
  // given
  val expectedUi = product1.toUi()
  whenever(searchProductsByCategoryUseCase("zapatos", "ofertas"))
   .thenReturn(listOf(product1))

  // when
  viewModel.searchProductByCategory("zapatos", "ofertas")

  // then
  advanceUntilIdle()
  assertEquals(UiState.Success(listOf(expectedUi)), viewModel.searchProductUiState.value)
 }

 @Test
 fun searchProductByCategory_failure_emitsError() = runTest {
  // given
  val exception = RuntimeException("failure")
  whenever(searchProductsByCategoryUseCase("zapatos", "ofertas"))
   .thenThrow(exception)
  whenever(errorMapper.mapError(exception)).thenReturn(R.string.error_unexpected)

  // when
  viewModel.searchProductByCategory("zapatos", "ofertas")

  // then
  advanceUntilIdle()
  val state = viewModel.searchProductUiState.value
  assertTrue(state is UiState.Error.MessageRes)
  assertEquals(R.string.error_unexpected, (state as UiState.Error.MessageRes).resId)
 }

 @Test
 fun onBackPressed_setsInitialState() = runTest {
  // when
  viewModel.onBackPressed()

  // then
  assertTrue(viewModel.searchProductUiState.value is UiState.Initial)
 }
}
