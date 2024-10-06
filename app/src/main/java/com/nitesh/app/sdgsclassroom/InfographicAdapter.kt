package com.nitesh.app.sdgsclassroom

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nitesh.app.sdgsclassroom.databinding.GoalInfoItemviewBinding

class InfographicAdapter(private val images: List<String>) : RecyclerView.Adapter<InfographicAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: GoalInfoItemviewBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            // Load the image using Glide or any image-loading library
            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(binding.infoImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GoalInfoItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}

