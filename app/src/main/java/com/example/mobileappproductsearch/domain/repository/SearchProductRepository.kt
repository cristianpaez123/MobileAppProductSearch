package com.example.mobileappproductsearch.domain.repository

import com.example.mobileappproductsearch.domain.model.ProductModelEntity

interface SearchProductRepository {
    suspend fun SearchProduct(keyword: String):List<ProductModelEntity>
}