package com.example.mobileappproductsearch.domain.useCase

import com.example.mobileappproductsearch.domain.model.ProductModelEntity
import com.example.mobileappproductsearch.domain.repository.SearchProductRepository
import javax.inject.Inject

class getProductsByCategoryUseCase @Inject constructor(private val repository: SearchProductRepository) {
    suspend operator fun invoke(keyword: String, category: String): List<ProductModelEntity> =
        repository.getProductsByCategory(keyword, category)
}