package com.example.mobileappproductsearch.ui.main.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.databinding.FragmentProductsListBinding
import com.example.mobileappproductsearch.ui.adapter.BestSellingProductsAdapter
import com.example.mobileappproductsearch.ui.adapter.CategoriesAdapter
import com.example.mobileappproductsearch.ui.adapter.ProductAdapter
import com.example.mobileappproductsearch.ui.model.CategoryModelUi
import com.example.mobileappproductsearch.ui.model.ProductUi
import com.example.mobileappproductsearch.utils.SuggestionSearchHelper
import com.example.mobileappproductsearch.utils.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsListFragment : Fragment() {

    private val viewModel: ProductsListViewModel by viewModels()

    private lateinit var binding: FragmentProductsListBinding

    private lateinit var bestSellingProductsAdapter: BestSellingProductsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var productAdapter: ProductAdapter

    private lateinit var popupHelper: SuggestionSearchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSuggestionPopup()
        initAdapters()
        setupProductRecyclerView()
        setupCategoriesRecyclerView()
        setupBestSellersRecyclerView()
        setupSearchView()
        setupObservers()

        viewModel.loadBestSellers()
    }

    private fun setupSuggestionPopup() {
        popupHelper = SuggestionSearchHelper(requireContext(), binding.cardView) { product ->
            binding.editTextSearchProduct.setText(product.name)
            Toast.makeText(requireContext(), "Seleccionado: ${product.name}", Toast.LENGTH_SHORT)
                .show()
            popupHelper.dismiss()
        }
    }

    private fun setupSearchView() {
        binding.editTextSearchProduct.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchProduct(v.text.toString().trim())
                popupHelper.dismiss()
                hideKeyboard()
                true
            } else false
        }

        binding.editTextSearchProduct.addTextChangedListener {
            binding.imageClear.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
            viewModel.loadSuggestions(it.toString())
        }

        binding.imageClear.setOnClickListener {
            binding.editTextSearchProduct.text.clear()
        }

        binding.imageSearch.setOnClickListener {
            viewModel.searchProduct(binding.editTextSearchProduct.text.toString().trim())
            popupHelper.dismiss()
            hideKeyboard()
        }
    }

    private fun initAdapters() {
        productAdapter = ProductAdapter(emptyList()) { selectedProduct ->
            navigateToProductDetails(selectedProduct)
        }
        categoriesAdapter = CategoriesAdapter(emptyList()) { selectedCategory ->
            val query = binding.editTextSearchProduct.text.toString().trim()
            viewModel.searchProductByCategory(query, selectedCategory.domainId)
        }
        bestSellingProductsAdapter = BestSellingProductsAdapter(emptyList()) { selectedProduct ->
            navigateToProductDetails(selectedProduct)
        }
    }

    private fun setupProductRecyclerView() = with(binding.recyclerProducts) {
        layoutManager = GridLayoutManager(context, 1)
        adapter = productAdapter
    }

    private fun setupCategoriesRecyclerView() = with(binding.categoriesRecycler) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = categoriesAdapter
    }

    private fun setupBestSellersRecyclerView() =
        with(binding.includeBestSellers.recyclerBestSellers) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = bestSellingProductsAdapter
        }


    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProductsListViewModel.SearchResultUiState.Loading -> loadingState()
                is ProductsListViewModel.SearchResultUiState.Success -> successState(state.products)
                is ProductsListViewModel.SearchResultUiState.Error -> errorState(state)
            }
        }
        viewModel.suggestions.observe(viewLifecycleOwner) { suggestions ->
            popupHelper.showSuggestions(suggestions)
            binding.editTextSearchProduct.requestFocus()
        }

        viewModel.uiCategories.observe(viewLifecycleOwner) { categories ->
            showCategories(categories)
        }

        viewModel.bestSellers.observe(viewLifecycleOwner) { products ->
            showBestSellers(products)
        }
    }

    private fun showBestSellers(products: List<ProductUi>) {
        showLoading(false)
        bestSellingProductsAdapter.updateData(products)
    }

    private fun showCategories(categories: List<CategoryModelUi>) {
        showLoading(false)
        binding.categoriesRecycler.visible(categories.size > 1)
        if (categories.size > 1) categoriesAdapter.updateCategories(categories)
    }

    private fun successState(products: List<ProductUi>) {
        showLoading(false)
        binding.includeBestSellers.root.visible(false)
        binding.recyclerProducts.visible(true)
        productAdapter.updateData(products)
    }

    private fun loadingState() = showLoading(true)

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun errorState(state: ProductsListViewModel.SearchResultUiState.Error) {
        showLoading(false)
        val message = state.messageRes?.let { getString(it) }
            ?: state.message
            ?: getString(R.string.error_unexpected)
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        popupHelper.dismiss()
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireContext())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun navigateToProductDetails(product: ProductUi) {
        val action = ProductsListFragmentDirections
            .actionProductsListFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }
}