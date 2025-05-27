package com.example.mobileappproductsearch.ui.main.productDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileappproductsearch.ui.model.PictureUi
import com.example.mobileappproductsearch.ui.model.ProductUi
import javax.inject.Inject

class ProductDetailViewModel @Inject constructor(): ViewModel(){

    private val _product = MutableLiveData<ProductUi>()
    val product: LiveData<ProductUi> = _product

    private val _images = MutableLiveData<List<PictureUi>>()
    val images: LiveData<List<PictureUi>> = _images

    fun setImages(pictures: List<PictureUi>) {
        _images.value = pictures
    }

    fun setProduct(product: ProductUi) {
        _product.value = product
    }
}