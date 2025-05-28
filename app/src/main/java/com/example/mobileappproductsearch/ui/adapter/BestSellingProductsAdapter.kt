package com.example.mobileappproductsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappproductsearch.databinding.ItemBestSellerProductsBinding
import com.bumptech.glide.Glide
import com.example.mobileappproductsearch.ui.model.ProductUi

class BestSellingProductsAdapter(
    private var products: List<ProductUi>,
    private val onProductSelected: (ProductUi) -> Unit
) : RecyclerView.Adapter<BestSellingProductsAdapter.BestSellerViewHolder>() {

    inner class BestSellerViewHolder(private val binding: ItemBestSellerProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUi) {
            binding.textTitle.text = product.name
            Glide.with(binding.imageProduct.context)
                .load(product.pictures.firstOrNull()?.url)
                .into(binding.imageProduct)
            binding.root.setOnClickListener {
                onProductSelected(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestSellerViewHolder {
        val binding = ItemBestSellerProductsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BestSellerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BestSellerViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newData: List<ProductUi>) {
        products = newData
        notifyDataSetChanged()
    }
}