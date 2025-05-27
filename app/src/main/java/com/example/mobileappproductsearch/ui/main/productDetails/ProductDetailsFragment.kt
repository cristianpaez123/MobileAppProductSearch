package com.example.mobileappproductsearch.ui.main.productDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.databinding.FragmentProductDetailsBinding
import com.example.mobileappproductsearch.databinding.FragmentProductsListBinding
import com.example.mobileappproductsearch.ui.adapter.AttributesAdapter
import com.example.mobileappproductsearch.ui.adapter.ProductAdapter
import com.example.mobileappproductsearch.ui.model.AttributeModelUi
import com.example.mobileappproductsearch.ui.model.ProductModelUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    private val args: ProductDetailsFragmentArgs by navArgs()
    private lateinit var attributesAdapter: AttributesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter(args.product.attributes)
        setupProductRecyclerView()
    }

    private fun initAdapter(attributes: List<AttributeModelUi>) {
        attributesAdapter = AttributesAdapter(attributes)
    }
    private fun setupObservers() {

    }

    private fun setupProductRecyclerView() = with(binding.recyclerViewAttributes) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = attributesAdapter
    }

}