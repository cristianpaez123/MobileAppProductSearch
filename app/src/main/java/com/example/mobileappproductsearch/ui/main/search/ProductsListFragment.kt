package com.example.mobileappproductsearch.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mobileappproductsearch.databinding.FragmentProductsListBinding
import com.example.mobileappproductsearch.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsListFragment : Fragment() {
    private lateinit var binding: FragmentProductsListBinding
    private val viewModel: ProductsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        //setupObservers()
    }

    private fun setupListeners() {
        binding.searchViewProducts.editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchProduct(v.text.toString().trim())
                true
            } else {
                false
            }
        }
    }

  /*  private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is ProductsListViewModel.SearchResultUiState.Loading -> loadingState()
                is ProductsListViewModel.SearchResultUiState.Success -> successState()
                is ProductsListViewModel.SearchResultUiState.Error -> errorState(state)
            }
        }
    }*/

}