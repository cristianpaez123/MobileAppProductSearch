package com.example.mobileappproductsearch.data.network

import com.example.mobileappproductsearch.data.model.CategoryModelDto
import com.example.mobileappproductsearch.data.model.ProductSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {
    @GET("products/search")
    suspend fun getProducts(
        @Query("status") status: String,
        @Query("site_id") siteId: String,
        @Query("q") query: String
    ): ProductSearchResponse

    @GET("sites/MCO/domain_discovery/search")
    suspend fun getCategories(
        @Query("q") query: String
    ): List<CategoryModelDto>

    @GET("products/search")
    suspend fun getProductsByCategory(
        @Query("status") status: String,
        @Query("site_id") siteId: String,
        @Query("q") query: String,
        @Query("domain_id") domainId: String
    ): ProductSearchResponse
}


