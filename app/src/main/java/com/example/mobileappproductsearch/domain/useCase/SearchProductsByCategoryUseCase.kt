package com.example.mobileappproductsearch.domain.useCase

import com.example.mobileappproductsearch.domain.model.Product
import com.example.mobileappproductsearch.domain.repository.ProductSearchRepository
import javax.inject.Inject

class SearchProductsByCategoryUseCase @Inject constructor(private val repository: ProductSearchRepository) {
    suspend operator fun invoke(keyword: String, category: String): List<Product> =
        repository.searchProductsByCategory(keyword, category)
}