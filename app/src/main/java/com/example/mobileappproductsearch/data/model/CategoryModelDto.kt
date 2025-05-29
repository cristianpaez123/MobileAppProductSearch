package com.example.mobileappproductsearch.data.model

import com.example.mobileappproductsearch.domain.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryModelDto(
    @SerializedName("domain_id") val domainId: String,
    @SerializedName("domain_name") val domainName: String
)

fun CategoryModelDto.toDomain(): Category = Category(
    domainId = domainId,
    domainName = domainName
)
