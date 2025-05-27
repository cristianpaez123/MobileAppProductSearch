package com.example.mobileappproductsearch.domain.repository.useCase

import com.example.mobileappproductsearch.domain.model.Product
import com.example.mobileappproductsearch.domain.repository.ProductSearchRepository
import javax.inject.Inject

class ProductsListUseCase @Inject constructor(private val repository: ProductSearchRepository) {
    suspend operator fun invoke(keyword: String): List<Product> = repository.searchProducts(keyword)
}