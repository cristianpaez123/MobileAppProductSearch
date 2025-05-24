package com.example.mobileappproductsearch.data.network

import com.example.mobileappproductsearch.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductsApi  {
    @GET("products/search")
    suspend fun getProducts(
        @Header("Authorization") authHeader: String,
        @Query("status") status: String,
        @Query("site_id") siteId: String,
        @Query("q") query: String
    ): ProductResponse
}