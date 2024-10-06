package com.nitesh.app.sdgsclassroom

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class AdminInfographicAdapter(private val infographicList: List<Uri>) : RecyclerView.Adapter<AdminInfographicAdapter.InfographicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfographicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_item_infographics, parent, false)
        return InfographicViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfographicViewHolder, position: Int) {
        holder.bind(infographicList[position])
    }

    override fun getItemCount(): Int {
        return infographicList.size
    }

    inner class InfographicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewInfographic)

        fun bind(uri: Uri) {
            Glide.with(itemView.context)
                .load(uri)
                .into(imageView)
        }
    }
}
