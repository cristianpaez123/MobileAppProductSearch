package com.example.mobileappproductsearch.data.repository

import com.example.mobileappproductsearch.data.model.toDomain
import com.example.mobileappproductsearch.data.network.ProductsApi
import com.example.mobileappproductsearch.domain.model.CategoryModelEntity
import com.example.mobileappproductsearch.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val api: ProductsApi
): CategoryRepository {
    override suspend fun getCategories(keyword: String): List<CategoryModelEntity> {
        val token = "Bearer APP_USR-7252043312902260-052709-66e3ddff00b4c094d519ace80351823f-706706141"
        val response = api.getCategories(token,keyword)
        return response.map { it.toDomain()  }
    }
}