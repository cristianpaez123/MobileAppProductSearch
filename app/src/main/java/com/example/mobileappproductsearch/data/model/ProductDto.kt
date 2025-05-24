package com.example.mobileappproductsearch.data.model

import com.example.mobileappproductsearch.domain.model.Attribute
import com.example.mobileappproductsearch.domain.model.Picture
import com.example.mobileappproductsearch.domain.model.ProductEntity

data class ProductDto(
    val id: String,
    val name: String,
    val status: String,
    val attributes: List<AttributeDto>,
    val pictures: List<PictureDto>,
    val description: String?
)

data class AttributeDto(
    val id: String,
    val name: String,
    val value_name: String
)

data class PictureDto(
    val id: String,
    val url: String
)

fun ProductDto.toDomain(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    status = status,
    attributes = attributes.map { it.toDomain() },
    pictures = pictures.map { it.toDomain() },
    description = description
)

fun AttributeDto.toDomain(): Attribute = Attribute(
    id = id,
    name = name,
    valueName = value_name
)

fun PictureDto.toDomain(): Picture = Picture(
    id = id,
    url = url
)