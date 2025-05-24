package com.example.mobileappproductsearch.data.repository

import com.example.mobileappproductsearch.data.model.ProductDto
import com.example.mobileappproductsearch.data.model.toDomain
import com.example.mobileappproductsearch.data.network.ProductsApi
import com.example.mobileappproductsearch.domain.model.ProductEntity
import com.example.mobileappproductsearch.domain.repository.SearchProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchProductRepositoryImpl @Inject constructor(
    private val api: ProductsApi
) : SearchProductRepository {

    override suspend fun SearchProduct(keyword: String): List<ProductEntity> {
        val token = "Bearer APP_USR-7252043312902260-052321-c74ef7e89d28dae46423fddfcf8438e8-706706141"
        val response = api.getProducts(token,"active", "MCO", keyword)
        return response.results.map { it.toDomain() }
    }
}