package com.example.loginpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginpage.data.AppDatabase
import com.example.loginpage.data.entity.CatatanEntity
import com.example.loginpage.databinding.FragmentNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FragmentNote : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        db = AppDatabase.getDatabase(requireContext())
        setupRecyclerView()
        
        binding.btnSimpan.setOnClickListener {
            saveNote()
        }
        
        fetchNotes()
    }

    private fun setupRecyclerView() {
        adapter = NoteAdapter(emptyList()) { note ->
            deleteNote(note)
        }
        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FragmentNote.adapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun fetchNotes() {
        lifecycleScope.launch {
            db.posyanduDao().getAllCatatan().collectLatest { list ->
                adapter.updateData(list)
            }
        }
    }

    private fun saveNote() {
        val judul = binding.etJudul.text.toString().trim()
        val isi = binding.etIsi.text.toString().trim()

        if (judul.isEmpty() || isi.isEmpty()) {
            Toast.makeText(requireContext(), "Judul dan isi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val tanggal = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date())
        val note = CatatanEntity(judul = judul, isi = isi, tanggal = tanggal)

        lifecycleScope.launch(Dispatchers.IO) {
            db.posyanduDao().insertCatatan(note)
            withContext(Dispatchers.Main) {
                binding.etJudul.text?.clear()
                binding.etIsi.text?.clear()
                Toast.makeText(requireContext(), "Catatan disimpan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteNote(note: CatatanEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.posyanduDao().deleteCatatan(note)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Catatan dihapus", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}