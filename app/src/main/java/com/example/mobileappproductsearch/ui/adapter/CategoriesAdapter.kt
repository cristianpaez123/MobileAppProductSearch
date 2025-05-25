package com.example.mobileappproductsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappproductsearch.databinding.ItemCategoryBinding
import com.example.mobileappproductsearch.ui.model.CategoryModelUi

class CategoriesAdapter(
    private var categories: List<CategoryModelUi>,
    private val onCategorySelected: (CategoryModelUi) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryModelUi) {
            binding.categoryName.text = category.domainName
            binding.root.isSelected = category.isSelected

            // Puedes cambiar color de fondo, texto, etc.
            binding.root.setOnClickListener {
                onCategorySelected(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newList: List<CategoryModelUi>) {
        categories = newList
        notifyDataSetChanged()
    }
}