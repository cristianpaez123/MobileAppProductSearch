package com.example.mobileappproductsearch.data.repository

import com.example.mobileappproductsearch.data.model.ProductDto
import com.example.mobileappproductsearch.data.model.ProductSearchResponse
import com.example.mobileappproductsearch.data.model.toDomain
import com.example.mobileappproductsearch.data.network.ProductsApi
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
class ProductSearchRepositoryImplTest {

    private lateinit var repository: ProductSearchRepositoryImpl
    private val api: ProductsApi = mock()

    private val productDto = ProductDto(
        id = "1",
        name = "Test Product",
        status = "active",
        attributes = emptyList(),
        pictures = emptyList(),
        description = "Sample description"
    )

    private val product = productDto.toDomain()

    @Before
    fun setup() {
        repository = ProductSearchRepositoryImpl(api)
    }

    @Test
    fun searchProducts_returnsMappedProducts() = runTest {
        // given
        val keyword = "shoes"
        val response = ProductSearchResponse(results = listOf(productDto))
        whenever(api.getProducts("active", "MCO", keyword)).thenReturn(response)

        // when
        val result = repository.searchProducts(keyword)

        // then
        verify(api).getProducts("active", "MCO", keyword)
        assertEquals(listOf(product), result)
    }

    @Test
    fun searchProducts_returnsEmptyList_whenNoResults() = runTest {
        // given
        val keyword = "empty"
        val response = ProductSearchResponse(results = emptyList())
        whenever(api.getProducts("active", "MCO", keyword)).thenReturn(response)

        // when
        val result = repository.searchProducts(keyword)

        // then
        verify(api).getProducts("active", "MCO", keyword)
        assertEquals(emptyList(), result)
    }

    @Test
    fun searchProductsByCategory_returnsMappedProducts() = runTest {
        // given
        val keyword = "electronics"
        val category = "cat123"
        val response = ProductSearchResponse(results = listOf(productDto))
        whenever(api.getProductsByCategory("active", "MCO", keyword, category)).thenReturn(response)

        // when
        val result = repository.searchProductsByCategory(keyword, category)

        // then
        verify(api).getProductsByCategory("active", "MCO", keyword, category)
        assertEquals(listOf(product), result)
    }

    @Test
    fun searchProductsByCategory_returnsEmptyList_whenNoResults() = runTest {
        // given
        val keyword = "unknown"
        val category = "cat000"
        val response = ProductSearchResponse(results = emptyList())
        whenever(api.getProductsByCategory("active", "MCO", keyword, category)).thenReturn(response)

        // when
        val result = repository.searchProductsByCategory(keyword, category)

        // then
        verify(api).getProductsByCategory("active", "MCO", keyword, category)
        assertEquals(emptyList(), result)
    }

    @Test
    fun searchProducts_throwsException_whenApiFails() = runTest {
        // given
        val keyword = "error"
        val exception = RuntimeException("API failure")
        whenever(api.getProducts("active", "MCO", keyword)).thenThrow(exception)

        // when
        val thrown = assertFailsWith<RuntimeException> {
            repository.searchProducts(keyword)
        }

        // then
        assertEquals("API failure", thrown.message)
    }

    @Test
    fun searchProductsByCategory_throwsException_whenApiFails() = runTest {
        // given
        val keyword = "error"
        val category = "any"
        val exception = RuntimeException("API failure by category")
        whenever(api.getProductsByCategory("active", "MCO", keyword, category)).thenThrow(exception)

        // when
        val thrown = assertFailsWith<RuntimeException> {
            repository.searchProductsByCategory(keyword, category)
        }

        // then
        assertEquals("API failure by category", thrown.message)
    }
}
