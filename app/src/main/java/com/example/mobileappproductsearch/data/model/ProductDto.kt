package com.example.mobileappproductsearch.data.model

import com.example.mobileappproductsearch.domain.model.Attribute
import com.example.mobileappproductsearch.domain.model.Picture
import com.example.mobileappproductsearch.domain.model.Product
import com.google.gson.annotations.SerializedName

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
    @SerializedName("value_name") val valueName: String
)

data class PictureDto(
    val id: String,
    val url: String
)

fun ProductDto.toDomain(): Product = Product(
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
    valueName = valueName
)

fun PictureDto.toDomain(): Picture = Picture(
    id = id,
    url = url
)