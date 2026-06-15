package com.example.loginpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginpage.data.entity.CatatanEntity

class CatatanAdapter(
    private var list: List<CatatanEntity>,
    private val onDelete: (CatatanEntity) -> Unit
) : RecyclerView.Adapter<CatatanAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJudul: TextView = view.findViewById(android.R.id.text1)
        val tvIsi: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvJudul.text = item.judul
        holder.tvIsi.text = "${item.tanggal}\n${item.isi}"
        
        holder.itemView.setOnLongClickListener {
            onDelete(item)
            true
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<CatatanEntity>) {
        list = newList
        notifyDataSetChanged()
    }
}