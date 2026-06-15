package com.example.loginpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginpage.data.entity.BalitaEntity

class BalitaAdapter(
    private var list: List<BalitaEntity>,
    private val onDelete: (BalitaEntity) -> Unit
) : RecyclerView.Adapter<BalitaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvDetail: TextView = view.findViewById(R.id.tvDetail)
        val btnDelete: ImageView = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_balita, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNama.text = item.nama
        holder.tvDetail.text = "Usia: ${item.usia} bln | BB: ${item.berat} kg | TB: ${item.tinggi} cm"
        
        holder.btnDelete.setOnClickListener {
            onDelete(item)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<BalitaEntity>) {
        list = newList
        notifyDataSetChanged()
    }
}