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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.databinding.FragmentProductsListBinding
import com.example.mobileappproductsearch.ui.adapter.CategoriesAdapter
import com.example.mobileappproductsearch.ui.adapter.ProductAdapter
import com.example.mobileappproductsearch.ui.adapter.SuggestionAdapter
import com.example.mobileappproductsearch.ui.model.CategoryModelUi
import com.example.mobileappproductsearch.ui.model.ProductModelUi
import com.example.mobileappproductsearch.utils.SuggestionSearchHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsListFragment : Fragment() {

    private lateinit var binding: FragmentProductsListBinding
    private val viewModel: ProductsListViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    lateinit var popupHelper: SuggestionSearchHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popupHelper = SuggestionSearchHelper(requireContext(), binding.cardView) { product ->
            binding.editextSearchProduct.setText(product.name)
            Toast.makeText(requireContext(), "Seleccionado: ${product.name}", Toast.LENGTH_SHORT)
                .show()
            popupHelper.dismiss()
        }
        initRecyclerView()
        setupSearchView()
        setupObservers()
    }

    private fun setupSearchView() {
        binding.editextSearchProduct.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchProduct(v.text.toString().trim())
                popupHelper.dismiss()
                hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.editextSearchProduct.addTextChangedListener {
            binding.imageClear.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
            val query = it.toString()
            viewModel.getSuggestions(query)
        }

        binding.imageClear.setOnClickListener {
            binding.editextSearchProduct.text.clear()
        }

        binding.imageSearch.setOnClickListener {
            viewModel.searchProduct(binding.editextSearchProduct.text.toString().trim())
            popupHelper.dismiss()
            hideKeyboard()
        }
    }

    private fun initRecyclerView() {
        productAdapter = ProductAdapter(emptyList())
        with(binding.recyclerProducts) {
            layoutManager = GridLayoutManager(this.context, 1)
            this.adapter = this@ProductsListFragment.productAdapter
        }

       categoriesAdapter = CategoriesAdapter(emptyList()) { selectedCategory ->

        }
        with(binding.categoriesRecycler) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = this@ProductsListFragment.categoriesAdapter
        }
    }


    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is ProductsListViewModel.SearchResultUiState.Loading -> loadingState()
                is ProductsListViewModel.SearchResultUiState.Success -> successState(state.products)
                is ProductsListViewModel.SearchResultUiState.Error -> errorState(state)
            }
        }
        viewModel.suggestions.observe(viewLifecycleOwner) { suggestions ->
            popupHelper.showSuggestions(suggestions)
        }

        viewModel.uiCategories.observe(viewLifecycleOwner){ categories ->
            categoriesShow(categories)
        }
    }

    private fun categoriesShow(categories: List<CategoryModelUi>) {
        showLoading(false)
        categoriesAdapter.updateCategories(categories)
    }

    private fun successState(products: List<ProductModelUi>) {
        showLoading(false)
        productAdapter.updateData(products)
    }

    private fun loadingState() = showLoading(true)

    private fun showLoading(show: Boolean) {
        binding.progressBar.setVisibility(if (show) View.VISIBLE else View.GONE)
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

    fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireContext())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}