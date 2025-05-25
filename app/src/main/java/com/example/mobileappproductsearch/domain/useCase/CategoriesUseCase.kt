package com.example.mobileappproductsearch.domain.useCase

import com.example.mobileappproductsearch.domain.model.CategoryModelEntity
import com.example.mobileappproductsearch.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoriesUseCase@Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(keyword: String): List<CategoryModelEntity> = repository.getCategories(keyword)
}