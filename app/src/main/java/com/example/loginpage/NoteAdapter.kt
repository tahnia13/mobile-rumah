package com.example.loginpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginpage.data.entity.CatatanEntity

class NoteAdapter(
    private var list: List<CatatanEntity>,
    private val onDelete: (CatatanEntity) -> Unit
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJudul: TextView = view.findViewById(R.id.tvJudul)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        val tvIsi: TextView = view.findViewById(R.id.tvIsi)
        val btnDelete: ImageView = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvJudul.text = item.judul
        holder.tvTanggal.text = item.tanggal
        holder.tvIsi.text = item.isi
        
        holder.btnDelete.setOnClickListener {
            onDelete(item)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<CatatanEntity>) {
        list = newList
        notifyDataSetChanged()
    }
}