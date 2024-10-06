package com.nitesh.app.sdgsclassroom.BottomFrag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nitesh.app.sdgsclassroom.R

class NewsAdapter(private val newsList: List<News>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsImage: ImageView = itemView.findViewById(R.id.news_image)
        val newsDescription: TextView = itemView.findViewById(R.id.news_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.newsDescription.text = news.description
        holder.newsImage.setImageResource(news.imageResId) // Replace with actual image loading (e.g., Glide)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}
