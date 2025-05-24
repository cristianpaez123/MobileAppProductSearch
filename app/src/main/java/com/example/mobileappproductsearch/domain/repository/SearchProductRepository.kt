package com.example.mobileappproductsearch.domain.repository

import com.example.mobileappproductsearch.domain.model.ProductEntity

interface SearchProductRepository {
    suspend fun SearchProduct(keyword: String):List<ProductEntity>
}