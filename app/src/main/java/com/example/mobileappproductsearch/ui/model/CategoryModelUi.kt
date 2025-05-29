package com.example.mobileappproductsearch.ui.model

import com.example.mobileappproductsearch.domain.model.Category

data class CategoryModelUi(
    val domainId: String,
    val domainName: String,
    val isSelected: Boolean = false
)

fun Category.toUi(): CategoryModelUi = CategoryModelUi(
    domainId = domainId,
    domainName = domainName
)
