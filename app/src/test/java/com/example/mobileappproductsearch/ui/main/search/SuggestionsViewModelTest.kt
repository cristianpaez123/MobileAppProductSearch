package com.example.mobileappproductsearch.ui.main.search

import com.example.mobileappproductsearch.domain.model.Product
import com.example.mobileappproductsearch.domain.useCase.SearchProductsUseCase
import com.example.mobileappproductsearch.ui.model.ProductUi
import com.example.mobileappproductsearch.ui.model.toUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SuggestionsViewModelTest {

 private lateinit var viewModel: SuggestionsViewModel
 private val searchProductsUseCase: SearchProductsUseCase = mock()
 private val testDispatcher = StandardTestDispatcher()

 @Before
 fun setup() {
  Dispatchers.setMain(testDispatcher)
  viewModel = SuggestionsViewModel(searchProductsUseCase)
 }

 @After
 fun tearDown() {
  Dispatchers.resetMain()
 }

 @Test
 fun loadSuggestions_success_emitsMappedProducts() = runTest {
  // given
  val expectedUi = product1.toUi()
  whenever(searchProductsUseCase("zapatos")).thenReturn(listOf(product1))

  // when
  viewModel.loadSuggestions("zapatos")

  // then
  advanceUntilIdle()
  assertEquals(listOf(expectedUi), viewModel.suggestions.value)
 }

 @Test
 fun loadSuggestions_failure_emitsEmptyList() = runTest {
  // given
  whenever(searchProductsUseCase("error")).thenThrow(RuntimeException("API failed"))

  // when
  viewModel.loadSuggestions("error")

  // then
  advanceUntilIdle()
  assertEquals(emptyList<ProductUi>(), viewModel.suggestions.value)
 }

 private val product1 = Product(
  id = "1",
  name = "Product 1",
  status = "active",
  attributes = emptyList(),
  pictures = emptyList(),
  description = "Desc 1"
 )
}
