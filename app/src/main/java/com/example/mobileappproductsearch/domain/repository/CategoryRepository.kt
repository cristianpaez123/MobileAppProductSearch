package com.example.mobileappproductsearch.domain.repository

import com.example.mobileappproductsearch.domain.model.CategoryModelEntity

interface CategoryRepository {
    suspend fun getCategories(keyword: String): List<CategoryModelEntity>
}