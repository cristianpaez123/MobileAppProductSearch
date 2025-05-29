package com.example.mobileappproductsearch.data.repository

import com.example.mobileappproductsearch.data.model.toDomain
import com.example.mobileappproductsearch.data.network.ProductsApi
import com.example.mobileappproductsearch.domain.model.Category
import com.example.mobileappproductsearch.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val api: ProductsApi
) : CategoryRepository {
    override suspend fun getCategories(keyword: String): List<Category> {
        val response = api.getCategories(keyword)
        return response.map { it.toDomain() }
    }
}