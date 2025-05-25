package com.example.mobileappproductsearch.data.network

import com.example.mobileappproductsearch.data.model.CategoryModelDto
import com.example.mobileappproductsearch.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductsApi {
    @GET("products/search")
    suspend fun getProducts(
        @Header("Authorization") authHeader: String,
        @Query("status") status: String,
        @Query("site_id") siteId: String,
        @Query("q") query: String
    ): ProductResponse

    @GET("sites/MCO/domain_discovery/search")
    suspend fun getCategories(
        @Header("Authorization") authHeader: String,
        @Query("q") query: String
    ): List<CategoryModelDto>

    @GET("products/search")
    suspend fun getProductsByCategory(
        @Header("Authorization") authHeader: String,
        @Query("status") status: String,
        @Query("site_id") siteId: String,
        @Query("q") query: String,
        @Query("domain_id") domainId: String
    ): ProductResponse
}


