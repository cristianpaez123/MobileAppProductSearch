package com.example.mobileappproductsearch.ui.main.bestSellers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mobileappproductsearch.databinding.ViewBestSellersBinding
import com.example.mobileappproductsearch.ui.adapter.BestSellingProductsAdapter
import com.example.mobileappproductsearch.ui.common.UiState
import com.example.mobileappproductsearch.ui.main.BestSellersListener
import com.example.mobileappproductsearch.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BestSellersFragment : Fragment() {

    private lateinit var binding: ViewBestSellersBinding
    private val viewModel: BestSellersViewModel by viewModels()

    private lateinit var adapter: BestSellingProductsAdapter

    private var bestSellersListener: BestSellersListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bestSellersListener = parentFragment as? BestSellersListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ViewBestSellersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        setupRecycler()
        observeUiState()
        viewModel.loadBestSellers()
    }

    private fun initAdapter() {
        adapter = BestSellingProductsAdapter(emptyList()) { product ->
            bestSellersListener?.onProductSelected(product)
        }
    }

    private fun setupRecycler() {
        binding.recyclerBestSellers.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@BestSellersFragment.adapter
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    showShimmer(false)
                    when (state) {
                        is UiState.Initial -> Unit
                        is UiState.EmptyData -> Unit
                        is UiState.Loading -> showShimmer(true)
                        is UiState.Success -> {
                            binding.root.visible(true)
                            adapter.updateData(state.data)
                        }
                        is UiState.Error -> {
                            bestSellersListener?.showError(state){
                                viewModel.loadBestSellers()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showShimmer(show: Boolean) {
        binding.includeShimmerBestSellers.shimmerBestSellers.visible(show)
    }

}
