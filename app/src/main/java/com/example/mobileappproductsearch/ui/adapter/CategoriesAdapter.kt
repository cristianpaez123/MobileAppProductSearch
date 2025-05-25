package com.example.mobileappproductsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappproductsearch.R
import com.example.mobileappproductsearch.databinding.ItemCategoryBinding
import com.example.mobileappproductsearch.ui.model.CategoryModelUi
import com.example.mobileappproductsearch.ui.model.ProductModelUi

class CategoriesAdapter(
    private var categories: List<CategoryModelUi>,
    private val onCategorySelected: (CategoryModelUi) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.categoryName.text = category.domainName

        // Cambiar estilos según si está seleccionada
        val context = holder.itemView.context
        if (category.isSelected) {
            holder.binding.categoryCard.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
        } else {
            holder.binding.categoryCard.setCardBackgroundColor(
                ContextCompat.getColor(context, R.color.categoryItemBackground)
            )
            holder.binding.categoryName.setTextColor(
                ContextCompat.getColor(context, android.R.color.black)
            )
        }

        holder.binding.root.setOnClickListener {
            // Actualizar selección
            updateSelectedCategory(category)
            onCategorySelected(category)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateSelectedCategory(selected: CategoryModelUi) {
        categories = categories.map {
            it.copy(isSelected = it.domainId == selected.domainId)
        }
        notifyDataSetChanged()
    }

    fun updateCategories(newList: List<CategoryModelUi>) {
        categories = newList
        notifyDataSetChanged()
    }
}