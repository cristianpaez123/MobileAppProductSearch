package com.example.mobileappproductsearch.data.repository

import com.example.mobileappproductsearch.data.model.CategoryModelDto
import com.example.mobileappproductsearch.data.model.toDomain
import com.example.mobileappproductsearch.data.network.ProductsApi
import com.example.mobileappproductsearch.domain.repository.CategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryRepositoryImplTest {

    private lateinit var repository: CategoryRepository
    private val api: ProductsApi = mock()

    @Before
    fun setup() {
        repository = CategoryRepositoryImpl(api)
    }

    @Test
    fun getCategories_returnsMappedCategories() = runTest {
        // given
        val keyword = "tecnologia"
        val dtoList = listOf(CategoryModelDto("cat1", "Categoria 1"))
        val expected = dtoList.map { it.toDomain() }

        whenever(api.getCategories(keyword)).thenReturn(dtoList)

        // when
        val result = repository.getCategories(keyword)

        // then
        verify(api).getCategories(keyword)
        assertEquals(expected, result)
    }

    @Test
    fun getCategories_whenApiReturnsEmpty_returnsEmptyList() = runTest {
        // given
        val keyword = "zapatos"
        whenever(api.getCategories(keyword)).thenReturn(emptyList())

        // when
        val result = repository.getCategories(keyword)

        // then
        verify(api).getCategories(keyword)
        assertEquals(emptyList(), result)
    }

    @Test
    fun getCategories_whenApiReturnsMultipleItems_returnsMappedList() = runTest {
        // given
        val dtoList = listOf(
            CategoryModelDto("cat1", "Categoria 1"),
            CategoryModelDto("cat2", "Categoria 2")
        )
        val keyword = "hogar"
        val expected = dtoList.map { it.toDomain() }

        whenever(api.getCategories(keyword)).thenReturn(dtoList)

        // when
        val result = repository.getCategories(keyword)

        // then
        verify(api).getCategories(keyword)
        assertEquals(expected, result)
    }
}
