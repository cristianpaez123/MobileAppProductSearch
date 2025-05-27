package com.example.mobileappproductsearch.ui.main.productDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileappproductsearch.ui.model.PictureModelUi
import com.example.mobileappproductsearch.ui.model.ProductModelUi
import javax.inject.Inject

class ProductDetailViewModel @Inject constructor(): ViewModel(){

    private val _product = MutableLiveData<ProductModelUi>()
    val product: LiveData<ProductModelUi> = _product

    private val _images = MutableLiveData<List<PictureModelUi>>()
    val images: LiveData<List<PictureModelUi>> = _images

    fun setImages(pictures: List<PictureModelUi>) {
        _images.value = pictures
    }

    fun setProduct(product: ProductModelUi) {
        _product.value = product
    }
}