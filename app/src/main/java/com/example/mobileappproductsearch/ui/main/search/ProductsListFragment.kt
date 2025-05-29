package com.example.mobileappproductsearch.ui.main.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
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
class ProductsListFragment : Fragment(), BestSellersListener, ProductsListViewBinder.Listener {

    private val viewModel: ProductsListViewModel by viewModels()

    private lateinit var viewBinder: ProductsListViewBinder

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinder = ProductsListViewBinder(inflater, container, this)
        return viewBinder.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackPressed()
                }
            }
        )
        initAdapters()
        observeUiState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinder.dismissSuggestions()
    }

    private fun initAdapters() {
        productAdapter = ProductAdapter(emptyList()) {
            navigateToProductDetails(it)
        }

        categoriesAdapter = CategoriesAdapter(emptyList()) {
            val query = viewBinder.getSearchText()
            viewModel.searchProductByCategory(query, it.domainId)
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
                    viewBinder.showSuggestions(it)
                }
            }
        }
    }

    private fun observeCategories() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect { viewBinder.showCategories(it) }
            }
        }
    }

    private fun handleSearchState(state: UiState<List<ProductUi>>) {
        viewBinder.hideError()
        viewBinder.showLoading(false)
        when (state) {
            is UiState.Initial -> {
                viewBinder.clearSearchField()
                loadBestSellersFragment()
            }

            is UiState.EmptyData -> viewBinder.showNoProductsFound()
            is UiState.Loading -> viewBinder.showLoading(true)
            is UiState.Success -> {
                viewBinder.showBestSellers(false)
                viewBinder.updateProducts(state.data)
            }

            is UiState.Error -> errorState(state) {
                submitSearch(viewBinder.getSearchText())
            }
        }
    }

    private fun errorState(error: UiState.Error, retryAction: () -> Unit) {
        val message = when (error) {
            is UiState.Error.MessageRes -> getString(error.resId)
            is UiState.Error.MessageText -> error.message
        }
        viewBinder.showError(message, retryAction)
    }

    override fun submitSearch(query: String) {
        val cleanQuery = query.trim()
        if (cleanQuery.isNotEmpty()) {
            viewModel.searchProduct(cleanQuery)
            hideKeyboard()
        }
    }

    override fun searchTextChanged(query: String) {
        viewModel.loadSuggestions(query)
    }

    override fun onSuggestionSelected(product: ProductUi) {
        navigateToProductDetails(product)
    }

    private fun loadBestSellersFragment() {
        viewBinder.showBestSellers(true)
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

    override fun onProductSelected(product: ProductUi) {
        navigateToProductDetails(product)
    }

    override fun onCategorySelected(query: String, categoryId: String) {
        viewModel.searchProductByCategory(query, categoryId)
    }

    override fun showError(error: UiState.Error, retryAction: () -> Unit) {
        errorState(error, retryAction)
    }
}
