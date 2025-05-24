package com.example.mobileappproductsearch.domain.repository.useCase

import com.example.mobileappproductsearch.domain.model.ProductModelEntity
import com.example.mobileappproductsearch.domain.repository.SearchProductRepository
import javax.inject.Inject

class ProductsListUseCase @Inject constructor(private val repository: SearchProductRepository) {
    suspend operator fun invoke(keyword: String): List<ProductModelEntity> = repository.SearchProduct(keyword)
}