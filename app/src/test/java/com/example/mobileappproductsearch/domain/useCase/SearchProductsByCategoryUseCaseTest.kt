package com.example.mobileappproductsearch.domain.useCase

import com.example.mobileappproductsearch.domain.model.Product
import com.example.mobileappproductsearch.domain.repository.ProductSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class SearchProductsByCategoryUseCaseTest {

 private lateinit var useCase: SearchProductsByCategoryUseCase
 private val repository: ProductSearchRepository = mock()

 private val product1 = Product(
  id = "1",
  name = "Zapato Deportivo",
  status = "active",
  attributes = emptyList(),
  pictures = emptyList(),
  description = "Para correr"
 )

 @Before
 fun setup() {
  useCase = SearchProductsByCategoryUseCase(repository)
 }

 @Test
 fun invoke_returnsProductsFromRepository() = runTest {
  // given
  val keyword = "zapatos"
  val category = "deporte"
  val expectedProducts = listOf(product1)
  whenever(repository.searchProductsByCategory(keyword, category)).thenReturn(expectedProducts)

  // when
  val result = useCase(keyword, category)

  // then
  verify(repository).searchProductsByCategory(keyword, category)
  assertEquals(expectedProducts, result)
 }

 @Test
 fun invoke_repositoryThrows_exceptionPropagates() = runTest {
  // given
  val keyword = "zapatos"
  val category = "formal"
  val exception = RuntimeException("API error")
  whenever(repository.searchProductsByCategory(keyword, category)).thenThrow(exception)

  // when + then
  val thrown = assertFailsWith<RuntimeException> {
   useCase(keyword, category)
  }

  assertEquals("API error", thrown.message)
  verify(repository).searchProductsByCategory(keyword, category)
 }
}
