package com.example.mobileappproductsearch.ui.main.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.databinding.FragmentProductsListBinding
import com.example.mobileappproductsearch.ui.adapter.CategoriesAdapter
import com.example.mobileappproductsearch.ui.adapter.ProductAdapter
import com.example.mobileappproductsearch.ui.model.CategoryModelUi
import com.example.mobileappproductsearch.ui.model.ProductUi
import com.example.mobileappproductsearch.utils.SuggestionSearchHelper
import com.example.mobileappproductsearch.utils.visible

class ProductsListViewBinder(
    inflater: LayoutInflater,
    container: ViewGroup?,
    private val listener: Listener,
) {

    private var binding: FragmentProductsListBinding =
        FragmentProductsListBinding.inflate(inflater, container, false)

    private val context = binding.root.context

    private lateinit var popupHelper: SuggestionSearchHelper

    private val productAdapter = ProductAdapter(emptyList()) { listener.onProductSelected(it) }

    private val categoriesAdapter = CategoriesAdapter(emptyList()) { category ->
        val query = binding.editTextSearchProduct.text.toString().trim()
        listener.onCategorySelected(query, category.domainId)
    }

    init {

        binding.recyclerProducts.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = productAdapter
        }

        binding.categoriesRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriesAdapter
        }

        setupSearchView()

        setupSuggestionPopup()
    }

    fun updateProducts(products: List<ProductUi>) {
        binding.recyclerProducts.visible(true)
        productAdapter.updateData(products)
    }

    fun showCategories(categories: List<CategoryModelUi>) {
        binding.categoriesRecycler.visible(categories.isNotEmpty())
        categoriesAdapter.updateCategories(categories)
    }

    fun showLoading(show: Boolean) {
        binding.includeLoadingOverlay.loadingOverlay.visible(show)
    }

    fun showBestSellers(visible: Boolean) {
        binding.bestSellersFragmentContainer.visible(visible)
    }

    fun showNoProductsFound() {
        binding.includeViewError.apply {
            errorView.visible(true)
            txtErrorMessage.text = context.getString(R.string.product_not_found)
            btnRetry.visible(false)
        }
    }

    fun showError(message: String, retryAction: () -> Unit) {
        binding.includeViewError.apply {
            errorView.visible(true)
            txtErrorMessage.text = message
            btnRetry.setOnClickListener {
                errorView.visible(false)
                retryAction()
            }
        }
    }

    fun hideError() {
        binding.includeViewError.errorView.visible(false)
    }

    fun clearSearchField() {
        binding.editTextSearchProduct.text.clear()
    }

    fun getRoot() = binding.root

    fun getSearchText() = binding.editTextSearchProduct.text.toString().trim()

    private fun setupSearchView() {
        binding.apply {
            editTextSearchProduct.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    listener.submitSearch(v.text.toString())
                    dismissSuggestions()
                    true
                } else false
            }

            editTextSearchProduct.addTextChangedListener {
                imageClear.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
                listener.searchTextChanged(it.toString())
            }

            imageClear.setOnClickListener {
                editTextSearchProduct.text.clear()
            }

            imageSearch.setOnClickListener {
                listener.submitSearch(editTextSearchProduct.text.toString())
                dismissSuggestions()
            }
        }
    }

    private fun setupSuggestionPopup() {
        popupHelper = SuggestionSearchHelper(context, binding.cardView) { product ->
            listener.onSuggestionSelected(product)
            binding.editTextSearchProduct.setText(product.name)
            clearSearchField()
            dismissSuggestions()
        }
    }

    fun showSuggestions(products: List<ProductUi>) {
        popupHelper.showSuggestions(products)
    }

    fun dismissSuggestions() {
        popupHelper.dismiss()
    }

    interface Listener {
        fun submitSearch(query: String)
        fun searchTextChanged(query: String)
        fun onSuggestionSelected(product: ProductUi)
        fun onProductSelected(product: ProductUi)
        fun onCategorySelected(query: String, categoryId: String)
    }
}
