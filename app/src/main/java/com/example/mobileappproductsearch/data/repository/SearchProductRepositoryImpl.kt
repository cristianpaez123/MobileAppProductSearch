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
        val token = "Bearer APP_USR-7252043312902260-052709-66e3ddff00b4c094d519ace80351823f-706706141"
        val response = api.getProducts(token,"active", "MCO", keyword)
        return response.results.map { it.toDomain() }
    }

    override suspend fun getProductsByCategory(keyword: String, category:String): List<ProductModelEntity> {
        val token = "Bearer APP_USR-7252043312902260-052709-66e3ddff00b4c094d519ace80351823f-706706141"
        val response = api.getProductsByCategory(token,"active", "MCO", keyword,category)
        return response.results.map { it.toDomain() }
    }
}