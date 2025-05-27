package com.example.mobileappproductsearch.ui.model

import android.os.Parcelable
import com.example.mobileappproductsearch.domain.model.AttributeModelEntity
import com.example.mobileappproductsearch.domain.model.PictureModelEntity
import com.example.mobileappproductsearch.domain.model.ProductModelEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModelUi(
    val id: String,
    val name: String,
    val status: String,
    val pictures: List<PictureModelUi>,
    val description: String?,
    val attributes: List<AttributeModelUi>
) : Parcelable

@Parcelize
data class AttributeModelUi(
    val id: String,
    val name: String,
    val valueName: String
): Parcelable

@Parcelize
data class PictureModelUi(
    val id: String,
    val url: String
): Parcelable

fun ProductModelEntity.toUiModel(): ProductModelUi {
    return ProductModelUi(
        id = id,
        name = name,
        status = status,
        pictures = pictures.map { it.toUiModel() },
        description = description,
        attributes = attributes.map { it.toUiModel() }
    )
}

fun AttributeModelEntity.toUiModel(): AttributeModelUi = AttributeModelUi(
    id = id,
    name = name,
    valueName = valueName
)

fun PictureModelEntity.toUiModel(): PictureModelUi = PictureModelUi(
    id = id,
    url = url
)