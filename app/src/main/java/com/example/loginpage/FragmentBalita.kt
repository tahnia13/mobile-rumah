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
import com.example.loginpage.data.entity.BalitaEntity
import com.example.loginpage.databinding.FragmentBalitaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentBalita : Fragment() {

    private var _binding: FragmentBalitaBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private lateinit var adapter: BalitaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBalitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        db = AppDatabase.getDatabase(requireContext())
        setupRecyclerView()
        
        binding.btnSimpan.setOnClickListener {
            saveBalita()
        }
        
        fetchBalita()
    }

    private fun setupRecyclerView() {
        adapter = BalitaAdapter(emptyList()) { balita ->
            deleteBalita(balita)
        }
        binding.rvBalita.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FragmentBalita.adapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun fetchBalita() {
        lifecycleScope.launch {
            db.posyanduDao().getAllBalita().collectLatest { list ->
                adapter.updateData(list)
            }
        }
    }

    private fun saveBalita() {
        val nama = binding.etNama.text.toString().trim()
        val usia = binding.etUsia.text.toString().trim()
        val berat = binding.etBerat.text.toString().trim()
        val tinggi = binding.etTinggi.text.toString().trim()

        if (nama.isEmpty() || usia.isEmpty() || berat.isEmpty() || tinggi.isEmpty()) {
            Toast.makeText(requireContext(), "Semua data harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val balita = BalitaEntity(nama = nama, usia = usia, berat = berat, tinggi = tinggi)

        lifecycleScope.launch(Dispatchers.IO) {
            db.posyanduDao().insertBalita(balita)
            withContext(Dispatchers.Main) {
                NotificationHelper(requireContext()).sendNotification(
                    "Pendaftaran Berhasil",
                    "Data balita $nama telah berhasil disimpan.",
                    "BALITA"
                )
                binding.etNama.text?.clear()
                binding.etUsia.text?.clear()
                binding.etBerat.text?.clear()
                binding.etTinggi.text?.clear()
                Toast.makeText(requireContext(), "Data Balita disimpan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteBalita(balita: BalitaEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.posyanduDao().deleteBalita(balita)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Data dihapus", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}