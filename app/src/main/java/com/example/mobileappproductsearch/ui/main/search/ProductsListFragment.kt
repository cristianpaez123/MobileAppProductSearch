package com.example.mobileappproductsearch.ui.main.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.databinding.FragmentProductsListBinding
import com.example.mobileappproductsearch.ui.adapter.BestSellingProductsAdapter
import com.example.mobileappproductsearch.ui.adapter.CategoriesAdapter
import com.example.mobileappproductsearch.ui.adapter.ProductAdapter
import com.example.mobileappproductsearch.ui.common.UiState
import com.example.mobileappproductsearch.ui.main.BestSellersListener
import com.example.mobileappproductsearch.ui.main.bestSellers.BestSellersFragment
import com.example.mobileappproductsearch.ui.model.CategoryModelUi
import com.example.mobileappproductsearch.ui.model.ProductUi
import com.example.mobileappproductsearch.utils.SuggestionSearchHelper
import com.example.mobileappproductsearch.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsListFragment : Fragment(), BestSellersListener {

    private val viewModel: ProductsListViewModel by viewModels()
    private lateinit var binding: FragmentProductsListBinding

    private lateinit var popupHelper: SuggestionSearchHelper
    private lateinit var bestSellingProductsAdapter: BestSellingProductsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        setupSuggestionPopup()
        setupRecyclerViews()
        setupSearchView()
        observeUiState()

        loadBestSellersFragment()

        viewModel.loadBestSellers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        popupHelper.dismiss()
    }

    private fun initAdapters() {
        productAdapter = ProductAdapter(emptyList()) {
            navigateToProductDetails(it)
            clearSearchField()
        }

        categoriesAdapter = CategoriesAdapter(emptyList()) {
            val query = binding.editTextSearchProduct.text.toString().trim()
            viewModel.searchProductByCategory(query, it.domainId)
        }

        bestSellingProductsAdapter = BestSellingProductsAdapter(emptyList()) {
            navigateToProductDetails(it)
            clearSearchField()
            popupHelper.dismiss()
        }
    }

    private fun setupSuggestionPopup() {
        popupHelper = SuggestionSearchHelper(requireContext(), binding.cardView) { product ->
            binding.editTextSearchProduct.setText(product.name)
            navigateToProductDetails(product)
            clearSearchField()
            popupHelper.dismiss()
        }
    }

    private fun setupRecyclerViews() {
        binding.recyclerProducts.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = productAdapter
        }

        binding.categoriesRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun setupSearchView() = binding.apply {
        editTextSearchProduct.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                submitSearch(v.text.toString())
                true
            } else false
        }

        editTextSearchProduct.addTextChangedListener {
            imageClear.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
            viewModel.loadSuggestions(it.toString())
        }

        imageClear.setOnClickListener {
            editTextSearchProduct.text.clear()
        }

        imageSearch.setOnClickListener {
            submitSearch(editTextSearchProduct.text.toString())
        }
    }

    private fun observeUiState() {
        observeSearchResults()
        observeSuggestions()
        observeCategories()
    }

    private fun observeSearchResults() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchProductUiState.collect { handleSearchState(it) }
            }
        }
    }

    private fun observeSuggestions() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.suggestions.collect {
                    popupHelper.showSuggestions(it)
                }
            }
        }
    }

    private fun observeCategories() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect { showCategories(it) }
            }
        }
    }

    private fun handleSearchState(state: UiState<List<ProductUi>>) {
        hideError()
        showLoading(false)
        when (state) {
            is UiState.Idle -> Unit
            is UiState.Loading -> showLoading(true)
            is UiState.Success -> {
                hideBestSellers()
                showProducts(state.data)
            }
            is UiState.Error -> errorState(state) {
                submitSearch(binding.editTextSearchProduct.text.toString())
            }
        }
    }

    private fun hideBestSellers() {
        binding.bestSellersFragmentContainer.visible(false)
    }

    private fun showProducts(products: List<ProductUi>) {
        binding.recyclerProducts.visible(true)
        productAdapter.updateData(products)
    }

    private fun showCategories(categories: List<CategoryModelUi>) {
        binding.categoriesRecycler.visible(categories.isNotEmpty())
        if (categories.isNotEmpty()) categoriesAdapter.updateCategories(categories)
    }

    private fun showLoading(show: Boolean) {
        binding.includeLoadingOverlay.loadingOverlay.visible(show)
    }

    private fun errorState(error: UiState.Error, retryAction: () -> Unit) {
        val message = when (error) {
            is UiState.Error.MessageRes -> getString(error.resId)
            is UiState.Error.MessageText -> error.message
        }
        showError(message, retryAction)
    }

    private fun showError(message: String, retryAction: () -> Unit) =
        binding.includeViewError.apply {
            errorView.visible(true)
            txtErrorMessage.text = message
            btnRetry.setOnClickListener { retryAction() }
        }

    private fun hideError() {
        binding.includeViewError.errorView.visible(false)
    }

    private fun submitSearch(query: String) {
        val cleanQuery = query.trim()
        if (cleanQuery.isNotEmpty()) {
            viewModel.searchProduct(cleanQuery)
            popupHelper.dismiss()
            hideKeyboard()
        }
    }

    private fun loadBestSellersFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.bestSellersFragmentContainer, BestSellersFragment())
            .commit()
    }

    private fun navigateToProductDetails(product: ProductUi) {
        val action = ProductsListFragmentDirections
            .actionProductsListFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireContext())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun clearSearchField() {
        binding.editTextSearchProduct.text.clear()
    }

    override fun onProductSelected(product: ProductUi) {
        navigateToProductDetails(product)
    }

    override fun showError(error: UiState.Error, retryAction: () -> Unit) {
        errorState(error, retryAction)
    }
}
