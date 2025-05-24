package com.example.mobileappproductsearch.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappproductsearch.databinding.PopupSuggestionsBinding
import com.example.mobileappproductsearch.ui.adapter.SuggestionAdapter
import com.example.mobileappproductsearch.ui.model.ProductModelUi

class SuggestionSearchHelper(
    private val context: Context,
    private val anchorView: EditText,
    private val onItemClick: (ProductModelUi) -> Unit
) {

    private var popupWindow: PopupWindow? = null

    fun showSuggestions(products: List<ProductModelUi>) {
        if (products.isEmpty()) {
            popupWindow?.dismiss()
            return
        }

        val binding = PopupSuggestionsBinding.inflate(LayoutInflater.from(context))
        val recyclerView = binding.recyclerSuggestions
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SuggestionAdapter(products, onItemClick)

        popupWindow?.dismiss()
        popupWindow = PopupWindow(
            binding.root,
            anchorView.width,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            elevation = 10f
            setBackgroundDrawable(ColorDrawable(Color.argb(230, 255, 255, 255)))
            isOutsideTouchable = true
            showAsDropDown(anchorView)
        }
    }

    fun dismiss() {
        popupWindow?.dismiss()
    }
}