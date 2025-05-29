package com.example.mobileappproductsearch.ui.model

import android.os.Parcelable
import com.example.mobileappproductsearch.domain.model.Attribute
import com.example.mobileappproductsearch.domain.model.Picture
import com.example.mobileappproductsearch.domain.model.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductUi(
    val id: String,
    val name: String,
    val status: String,
    val pictures: List<PictureUi>,
    val description: String?,
    val attributes: List<AttributeUi>
) : Parcelable

@Parcelize
data class AttributeUi(
    val id: String,
    val name: String,
    val valueName: String
) : Parcelable

@Parcelize
data class PictureUi(
    val id: String,
    val url: String
) : Parcelable

fun Product.toUi(): ProductUi {
    return ProductUi(
        id = id,
        name = name,
        status = status,
        pictures = pictures.map { it.toUi() },
        description = description,
        attributes = attributes.map { it.toUi() }
    )
}

fun Attribute.toUi(): AttributeUi = AttributeUi(
    id = id,
    name = name,
    valueName = valueName
)

fun Picture.toUi(): PictureUi = PictureUi(
    id = id,
    url = url
)