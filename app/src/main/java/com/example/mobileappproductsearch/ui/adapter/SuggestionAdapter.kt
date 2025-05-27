package com.example.mobileappproductsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileappproductsearch.databinding.ItemSuggestionBinding
import com.example.mobileappproductsearch.ui.model.ProductUi

class SuggestionAdapter(
    private val items: List<ProductUi>,
    private val onClick: (ProductUi) -> Unit
) : RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUi) {
            binding.textSuggestion.text = product.name
            val imageUrl = product.pictures.firstOrNull()?.url
            Glide.with(binding.imageThumbnail.context)
                .load(imageUrl)
                .into(binding.imageThumbnail)

            binding.root.setOnClickListener { onClick(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSuggestionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
}
