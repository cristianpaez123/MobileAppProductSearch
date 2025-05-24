package com.example.mobileappproductsearch.ui.model

import com.example.mobileappproductsearch.domain.model.ProductModelEntity

data class ProductModelUi(
    val id: String,
    val name: String,
    val status: String,
    val mainImageUrl: String?,
    val description: String?,
    val attributes: List<String>
)

fun ProductModelEntity.toUiModel(): ProductModelUi {
    return ProductModelUi(
        id = id,
        name = name,
        status = status,
        mainImageUrl = pictures.firstOrNull()?.url,
        description = description,
        attributes = attributes.map { "${it.name}: ${it.valueName}" }
    )
}
