package com.example.mobileappproductsearch.domain.useCase

import com.example.mobileappproductsearch.domain.model.Category
import com.example.mobileappproductsearch.domain.repository.CategoryRepository
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
class CategoriesUseCaseTest {

 private lateinit var useCase: CategoriesUseCase
 private val repository: CategoryRepository = mock()

 private val category1 = Category(
  domainId = "cat1",
  domainName = "Category 1"
 )

 @Before
 fun setup() {
  useCase = CategoriesUseCase(repository)
 }

 @Test
 fun invoke_returnsCategoriesFromRepository() = runTest {
  // given
  val keyword = "ropa"
  val expectedCategories = listOf(category1)
  whenever(repository.getCategories(keyword)).thenReturn(expectedCategories)

  // when
  val result = useCase(keyword)

  // then
  verify(repository).getCategories(keyword)
  assertEquals(expectedCategories, result)
 }

 @Test
 fun invoke_repositoryThrows_exceptionPropagates() = runTest {
  // given
  val keyword = "zapatos"
  val exception = RuntimeException("network error")
  whenever(repository.getCategories(keyword)).thenThrow(exception)

  // when + then
  val thrown = assertFailsWith<RuntimeException> {
   useCase(keyword)
  }

  assertEquals("network error", thrown.message)
  verify(repository).getCategories(keyword)
 }
}
