package com.example.mobileappproductsearch.data.repository

import com.example.mobileappproductsearch.data.model.toDomain
import com.example.mobileappproductsearch.data.network.ProductsApi
import com.example.mobileappproductsearch.domain.model.Product
import com.example.mobileappproductsearch.domain.repository.ProductSearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductSearchRepositoryImpl @Inject constructor(
    private val api: ProductsApi
) : ProductSearchRepository {

    override suspend fun searchProducts(keyword: String): List<Product> {
        val response = api.getProducts(STATUS_ACTIVE, SITE_ID_MCO, keyword)
        return response.results.map { it.toDomain() }
    }

    override suspend fun searchProductsByCategory(
        keyword: String,
        category: String
    ): List<Product> {
        val response = api.getProductsByCategory(STATUS_ACTIVE, SITE_ID_MCO, keyword, category)
        return response.results.map { it.toDomain() }
    }

    private companion object {
        const val STATUS_ACTIVE = "active"
        const val SITE_ID_MCO = "MCO"
    }
}