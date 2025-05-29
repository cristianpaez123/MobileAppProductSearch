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
class SearchProductsUseCaseTest {

 private lateinit var useCase: SearchProductsUseCase
 private val repository: ProductSearchRepository = mock()

 private val product1 = Product(
  id = "1",
  name = "Zapato Casual",
  status = "active",
  attributes = emptyList(),
  pictures = emptyList(),
  description = "Comodidad para el día a día"
 )

 @Before
 fun setup() {
  useCase = SearchProductsUseCase(repository)
 }

 @Test
 fun invoke_returnsProductsFromRepository() = runTest {
  // given
  val keyword = "zapatos"
  val expectedProducts = listOf(product1)
  whenever(repository.searchProducts(keyword)).thenReturn(expectedProducts)

  // when
  val result = useCase(keyword)

  // then
  verify(repository).searchProducts(keyword)
  assertEquals(expectedProducts, result)
 }

 @Test
 fun invoke_repositoryThrows_exceptionPropagates() = runTest {
  // given
  val keyword = "botas"
  val exception = RuntimeException("connection timeout")
  whenever(repository.searchProducts(keyword)).thenThrow(exception)

  // when + then
  val thrown = assertFailsWith<RuntimeException> {
   useCase(keyword)
  }

  assertEquals("connection timeout", thrown.message)
  verify(repository).searchProducts(keyword)
 }
}
