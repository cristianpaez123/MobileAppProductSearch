package com.example.mobileappproductsearch.domain.model

data class ProductModelEntity(
    val id: String,
    val name: String,
    val status: String,
    val attributes: List<AttributeModelEntity>,
    val pictures: List<PictureModelEntity>,
    val description: String?
)

data class AttributeModelEntity(
    val id: String,
    val name: String,
    val valueName: String
)

data class PictureModelEntity(
    val id: String,
    val url: String
)
