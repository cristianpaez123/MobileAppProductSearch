package com.example.mobileappproductsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileappproductsearch.databinding.ItemProductBinding
import com.example.mobileappproductsearch.ui.model.ProductModelUi

class ProductAdapter(
    private var products: List<ProductModelUi>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductModelUi) {
            binding.tvName.text = product.name
            binding.tvStatus.text = product.status
            Glide.with(binding.imgProduct.context)
                .load(product.mainImageUrl)
                .into(binding.imgProduct)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newData: List<ProductModelUi>) {
        products = newData
        notifyDataSetChanged()
    }
}