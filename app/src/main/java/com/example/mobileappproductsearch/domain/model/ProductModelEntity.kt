package com.example.mobileappproductsearch.domain.model

data class ProductModelEntity(
    val id: String,
    val name: String,
    val status: String,
    val attributes: List<Attribute>,
    val pictures: List<Picture>,
    val description: String?
)

data class Attribute(
    val id: String,
    val name: String,
    val valueName: String
)

data class Picture(
    val id: String,
    val url: String
)
