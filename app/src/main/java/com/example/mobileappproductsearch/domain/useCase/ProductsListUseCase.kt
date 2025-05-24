package com.example.mobileappproductsearch.domain.repository.useCase

import com.example.mobileappproductsearch.domain.model.ProductEntity
import com.example.mobileappproductsearch.domain.repository.SearchProductRepository
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class ProductsListUseCase @Inject constructor(private val repository: SearchProductRepository) {
    suspend operator fun invoke(keyword: String): List<ProductEntity> = repository.SearchProduct(keyword)
}