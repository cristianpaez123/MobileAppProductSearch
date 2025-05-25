package com.example.mobileappproductsearch.ui.model

import com.example.mobileappproductsearch.domain.model.CategoryModelEntity

data class CategoryModelUi(
    val domainId: String,
    val domainName: String,
    val isSelected: Boolean = false
)

fun CategoryModelEntity.toUiModel(): CategoryModelUi = CategoryModelUi(
    domainId = domainId,
    domainName = domainName
)
