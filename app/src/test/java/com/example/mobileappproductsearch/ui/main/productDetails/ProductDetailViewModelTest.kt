package com.example.mobileappproductsearch.ui.main.productDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobileappproductsearch.ui.model.PictureUi
import com.example.mobileappproductsearch.ui.model.ProductUi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Before
import org.junit.Test

class ProductDetailViewModelTest {

 @get:Rule
 val instantTaskExecutorRule = InstantTaskExecutorRule()

 private lateinit var viewModel: ProductDetailViewModel

 @Before
 fun setup() {
  viewModel = ProductDetailViewModel()
 }

 @Test
 fun setProduct_setsValueCorrectly() {
  // given
  val product = ProductUi(
   id = "1",
   name = "Product 1",
   status = "active",
   attributes = emptyList(),
   pictures = emptyList(),
   description = "Test product"
  )

  // when
  viewModel.setProduct(product)

  // then
  assertEquals(product, viewModel.product.value)
 }

 @Test
 fun setImages_setsValueCorrectly() {
  // given
  val images = listOf(
   PictureUi(id= "id1", url = "https://example.com/image1.jpg"),
   PictureUi(id= "id2", url = "https://example.com/image2.jpg")
  )

  // when
  viewModel.setImages(images)

  // then
  assertEquals(images, viewModel.images.value)
 }
}
