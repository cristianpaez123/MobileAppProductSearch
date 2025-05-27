package com.example.mobileappproductsearch.ui.main.productDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.mobileappproductsearch.databinding.FragmentProductDetailsBinding
import com.example.mobileappproductsearch.ui.adapter.AttributesAdapter
import com.example.mobileappproductsearch.ui.adapter.ImagePagerAdapter
import com.example.mobileappproductsearch.ui.model.AttributeUi
import com.example.mobileappproductsearch.ui.model.ProductUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    private val args: ProductDetailsFragmentArgs by navArgs()
    private lateinit var attributesAdapter: AttributesAdapter
    private lateinit var imagePagerAdapter: ImagePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setImages(args.product.pictures)
        viewModel.setProduct(args.product)
        setupObservers()
        initAdapter(args.product.attributes)
        setupProductRecyclerView()
    }

    private fun initAdapter(attributes: List<AttributeUi>) {
        attributesAdapter = AttributesAdapter(attributes)
        imagePagerAdapter = ImagePagerAdapter(emptyList())
        binding.viewPagerImages.adapter = imagePagerAdapter
    }

    private fun setupObservers() {
        viewModel.product.observe(viewLifecycleOwner) { product ->
            bindProductData(product)
        }

        viewModel.images.observe(viewLifecycleOwner) { images ->
            imagePagerAdapter.updateImages(images)
            binding.textViewCounter.text =
                if (images.isNotEmpty()) "1 / ${images.size}" else "0 / 0"
        }

        binding.viewPagerImages.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val total = imagePagerAdapter.itemCount
                binding.textViewCounter.text = "${position + 1} / $total"
            }
        })
    }

    private fun bindProductData(product: ProductUi) = with(binding) {
        textProductName.text = product.name
        setupListeners()
    }

    private fun setupProductRecyclerView() = with(binding.recyclerViewAttributes) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = attributesAdapter
    }

    private fun setupListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}