package com.example.loginpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PosyanduAdapter(
    private var items: List<PosyanduItem>,
    private val onItemClick: (PosyanduItem) -> Unit
) : RecyclerView.Adapter<PosyanduAdapter.PosyanduViewHolder>() {

    class PosyanduViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSubTitle: TextView = view.findViewById(R.id.tvSubTitle)
        val tvTag: TextView = view.findViewById(R.id.tvTag)
        val tvDetail1: TextView = view.findViewById(R.id.tvDetail1)
        val tvDetail2: TextView = view.findViewById(R.id.tvDetail2)
        val tvDetail3: TextView = view.findViewById(R.id.tvDetail3)
        val ivIcon: ImageView = view.findViewById(R.id.ivItemIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosyanduViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_posyandu, parent, false)
        return PosyanduViewHolder(view)
    }

    override fun onBindViewHolder(holder: PosyanduViewHolder, position: Int) {
        val item = items[position]
        
        holder.tvTitle.text = item.title
        
        // SubTitle
        if (item.subTitle.isNotEmpty()) {
            holder.tvSubTitle.visibility = View.VISIBLE
            holder.tvSubTitle.text = item.subTitle
        } else {
            holder.tvSubTitle.visibility = View.GONE
        }

        // Tag
        if (item.tag.isNotEmpty()) {
            holder.tvTag.visibility = View.VISIBLE
            holder.tvTag.text = item.tag
        } else {
            holder.tvTag.visibility = View.GONE
        }

        // Detail 1
        if (item.detail1.isNotEmpty()) {
            holder.tvDetail1.visibility = View.VISIBLE
            holder.tvDetail1.text = item.detail1
        } else {
            holder.tvDetail1.visibility = View.GONE
        }

        // Detail 2
        if (item.detail2.isNotEmpty()) {
            holder.tvDetail2.visibility = View.VISIBLE
            holder.tvDetail2.text = item.detail2
        } else {
            holder.tvDetail2.visibility = View.GONE
        }

        // Detail 3
        if (item.detail3.isNotEmpty()) {
            holder.tvDetail3.visibility = View.VISIBLE
            holder.tvDetail3.text = item.detail3
        } else {
            holder.tvDetail3.visibility = View.GONE
        }
        
        if (item.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.posdes)
                .error(R.drawable.posdes)
                .into(holder.ivIcon)
        } else {
            holder.ivIcon.setImageResource(R.drawable.posdes)
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<PosyanduItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}