package com.example.mobileappproductsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappproductsearch.databinding.ItemAttributeBinding
import com.example.mobileappproductsearch.ui.model.AttributeUi
import com.example.mobileappproductsearch.utils.resolveRowColor

class AttributesAdapter(
    private var attributes: List<AttributeUi>
) : RecyclerView.Adapter<AttributesAdapter.AttributeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAttributeBinding.inflate(inflater, parent, false)
        return AttributeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
        holder.bind(attributes[position], isGray = position % 2 != 0)
    }

    override fun getItemCount(): Int = attributes.size

    inner class AttributeViewHolder(
        private val binding: ItemAttributeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(attribute: AttributeUi, isGray: Boolean) = with(binding) {
            tvAttributeName.text = attribute.name
            tvAttributeValue.text = attribute.valueName
            root.setBackgroundColor(root.context.resolveRowColor(isGray))
        }
    }
}