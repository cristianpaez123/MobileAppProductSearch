package com.example.mobileappproductsearch.domain.repository

import com.example.mobileappproductsearch.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(keyword: String): List<Category>
}