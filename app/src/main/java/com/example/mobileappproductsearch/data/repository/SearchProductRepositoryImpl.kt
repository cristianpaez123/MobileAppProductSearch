package com.example.mobileappproductsearch.data.repository

import com.example.mobileappproductsearch.data.model.toDomain
import com.example.mobileappproductsearch.data.network.ProductsApi
import com.example.mobileappproductsearch.domain.model.ProductModelEntity
import com.example.mobileappproductsearch.domain.repository.SearchProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchProductRepositoryImpl @Inject constructor(
    private val api: ProductsApi
) : SearchProductRepository {

    override suspend fun SearchProduct(keyword: String): List<ProductModelEntity> {
        val token = "Bearer APP_USR-7252043312902260-052412-33d15f3bd43eba2cb05ea954010d9ac9-706706141"
        val response = api.getProducts(token,"active", "MCO", keyword)
        return response.results.map { it.toDomain() }
    }
}