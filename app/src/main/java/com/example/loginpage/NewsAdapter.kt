package com.example.loginpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(private val newsList: List<NewsPost>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivThumbnail: ImageView = view.findViewById(R.id.ivThumbnail)
        val tvTitle: TextView = view.findViewById(R.id.tvNewsTitle)
        val tvDate: TextView = view.findViewById(R.id.tvNewsDate)
        val tvDesc: TextView = view.findViewById(R.id.tvNewsDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.tvTitle.text = news.title
        holder.tvDate.text = news.pubDate
        holder.tvDesc.text = news.description

        Glide.with(holder.itemView.context)
            .load(news.thumbnail)
            .placeholder(R.drawable.posdes)
            .into(holder.ivThumbnail)
    }

    override fun getItemCount(): Int = newsList.size
}