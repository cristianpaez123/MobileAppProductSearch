package com.example.mobileappproductsearch.data.model

import com.example.mobileappproductsearch.domain.model.AttributeModelEntity
import com.example.mobileappproductsearch.domain.model.PictureModelEntity
import com.example.mobileappproductsearch.domain.model.ProductModelEntity
import com.google.gson.annotations.SerializedName

data class ProductModelDto(
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

fun ProductModelDto.toDomain(): ProductModelEntity = ProductModelEntity(
    id = id,
    name = name,
    status = status,
    attributes = attributes.map { it.toDomain() },
    pictures = pictures.map { it.toDomain() },
    description = description
)

fun AttributeDto.toDomain(): AttributeModelEntity = AttributeModelEntity(
    id = id,
    name = name,
    valueName = valueName
)

fun PictureDto.toDomain(): PictureModelEntity = PictureModelEntity(
    id = id,
    url = url
)