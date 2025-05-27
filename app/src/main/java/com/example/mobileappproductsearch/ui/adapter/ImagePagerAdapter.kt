package com.example.mobileappproductsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileappproductsearch.databinding.ItemImageBinding
import com.example.mobileappproductsearch.ui.model.PictureUi

class ImagePagerAdapter(
    private var images: List<PictureUi>
) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val picture = images[position]
        Glide.with(holder.binding.imageViewItem.context)
            .load(picture.url)
            .into(holder.binding.imageViewItem)
    }

    override fun getItemCount(): Int = images.size

    fun updateImages(newList: List<PictureUi>) {
        images = newList
        notifyDataSetChanged()
    }
}