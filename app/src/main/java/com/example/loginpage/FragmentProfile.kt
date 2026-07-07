package com.example.loginpage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.loginpage.databinding.FragmentProfileBinding

class FragmentProfile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Isi data profil pengembang
        binding.tvDeveloperName.text = "Tahnia"
        binding.tvNim.text = "2457301141"
        binding.tvProdi.text = "Sistem Informasi"
        binding.tvUniversitas.text = "Politeknik Caltex Riau"
        binding.tvEmail.text = "tahnia@gmail.com"
        binding.tvVersion.text = "Version 1.0.0"

        binding.tvDeskripsi.text = """
            Halo! Saya adalah mahasiswa Sistem Informasi yang memiliki minat besar dalam pengembangan aplikasi mobile, terutama Android. 
            
            Aplikasi Bina Desa ini dikembangkan sebagai bagian dari proyek akhir untuk memenuhi tugas mata kuliah Pemrograman Mobile.
            
            Saya berkomitmen untuk terus belajar dan mengembangkan aplikasi yang bermanfaat bagi masyarakat, khususnya dalam mendukung program Bina Desa dan pemberdayaan masyarakat.
            
            Teknologi yang digunakan:
            • Kotlin
            • Android SDK
            • Material Design
            • SharedPreferences
            • Fragment
            • Chip & ChipGroup
            • GridLayout
            • ListView/SimpleAdapter
        """.trimIndent()

        setupStackList()

        // Tombol Kembali ke Beranda
        binding.btnBackToHome.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentHomePosyandu())
                .commit()
            (activity as? MainActivity2)?.apply {
                supportActionBar?.title = "Posyandu Desa"
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }

        // Tambahkan tombol Settings jika ada di layout
        val btnSettings = view.findViewById<Button>(R.id.btnSettings)
        btnSettings?.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupStackList() {
        val data = listOf(
            mapOf("tech" to "Kotlin", "desc" to "Bahasa pemrograman utama"),
            mapOf("tech" to "Room Database", "desc" to "Penyimpanan data lokal"),
            mapOf("tech" to "Retrofit", "desc" to "Networking & API"),
            mapOf("tech" to "Glide", "desc" to "Image loading library"),
            mapOf("tech" to "Material Design", "desc" to "Komponen UI modern")
        )

        val adapter = SimpleAdapter(
            requireContext(),
            data,
            android.R.layout.simple_list_item_2,
            arrayOf("tech", "desc"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        binding.listViewStack.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}