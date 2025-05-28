package com.example.mobileappproductsearch.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappproductsearch.databinding.PopupSuggestionsBinding
import com.example.mobileappproductsearch.ui.adapter.SuggestionAdapter
import com.example.mobileappproductsearch.ui.model.ProductUi

class SuggestionSearchHelper(
    private val context: Context,
    private val anchorView: View,
    private val onItemClick: (ProductUi) -> Unit
) {
    private var popupWindow: PopupWindow? = null

    fun showSuggestions(products: List<ProductUi>) {
        if (products.isEmpty()) {
            dismiss()
            return
        }

        val binding = PopupSuggestionsBinding.inflate(LayoutInflater.from(context))
        setupRecycler(binding.recyclerSuggestions, products)
        createAndShowPopup(binding.root)
        adjustRecyclerHeight(binding.recyclerSuggestions)
    }

    private fun setupRecycler(recyclerView: RecyclerView, products: List<ProductUi>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SuggestionAdapter(products, onItemClick)
    }

    private fun createAndShowPopup(contentView: View) {
        popupWindow?.dismiss()
        popupWindow = PopupWindow(
            contentView,
            anchorView.width,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            false
        ).apply {
            elevation = 10f
            setBackgroundDrawable(ColorDrawable(Color.WHITE))
            isOutsideTouchable = true
            showAsDropDown(anchorView)
        }
    }

    private fun adjustRecyclerHeight(recyclerView: RecyclerView) {
        recyclerView.post {
            val maxPopupHeight = Resources.getSystem().displayMetrics.heightPixels / 3

            recyclerView.measure(
                View.MeasureSpec.makeMeasureSpec(anchorView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )

            val realHeight = recyclerView.measuredHeight
            recyclerView.layoutParams = recyclerView.layoutParams.apply {
                height = if (realHeight > maxPopupHeight) maxPopupHeight else ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    fun dismiss() {
        popupWindow?.dismiss()
    }
}
