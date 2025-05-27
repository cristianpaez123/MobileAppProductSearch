package com.example.mobileappproductsearch.domain.repository

import com.example.mobileappproductsearch.domain.model.Product

interface ProductSearchRepository {
    suspend fun searchProducts(keyword: String):List<Product>
    suspend fun searchProductsByCategory(keyword: String, category:String):List<Product>
}