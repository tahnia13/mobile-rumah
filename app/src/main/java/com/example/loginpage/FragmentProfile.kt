package com.example.loginpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.tvNim.text = "20220040080"
        binding.tvProdi.text = "Teknik Informatika"
        binding.tvUniversitas.text = "Universitas Nusa Putra"
        binding.tvEmail.text = "tahnia@gmail.com"
        binding.tvVersion.text = "Version 1.0.0"

        binding.tvDeskripsi.text = """
            Halo! Saya adalah mahasiswa Teknik Informatika yang memiliki minat besar dalam pengembangan aplikasi mobile, terutama Android. 
            
            Aplikasi ini dikembangkan sebagai bagian dari proyek akhir untuk memenuhi tugas mata kuliah Pemrograman Mobile.
            
            Saya berkomitmen untuk terus belajar dan mengembangkan aplikasi yang bermanfaat bagi masyarakat, khususnya dalam mendukung program Bina Desa dan pemberdayaan masyarakat.
            
            Teknologi yang digunakan:
            • Kotlin
            • Android SDK
            • Material Design
            • SharedPreferences
            • Fragment
            • REST API
        """.trimIndent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}