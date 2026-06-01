package com.example.loginpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.loginpage.databinding.FragmentSettingsBinding

class FragmentSettings : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListView()
    }

    private fun setupListView() {
        val menuItems = arrayOf(
            "📋 Kebijakan Privasi",
            "🔒 Syarat & Ketentuan",
            "ℹ️ Tentang Aplikasi",
            "⭐ Beri Rating",
            "📞 Hubungi Kami",
            "🔄 Versi Aplikasi"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            menuItems
        )

        binding.listViewSettings.adapter = adapter

        binding.listViewSettings.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> showPrivacyPolicy()
                1 -> showTermsOfService()
                2 -> showAboutApp()
                3 -> Toast.makeText(requireContext(), "Terima kasih atas rating Anda!", Toast.LENGTH_SHORT).show()
                4 -> showContactInfo()
                5 -> Toast.makeText(requireContext(), "Bina Desa - Versi 1.0.0", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPrivacyPolicy() {
        AlertDialog.Builder(requireContext())
            .setTitle("Kebijakan Privasi")
            .setMessage("""
                Bina Desa berkomitmen melindungi data pribadi Anda. Data yang dikumpulkan:
                
                • Informasi profil pengguna
                • Data penduduk desa
                • Laporan kegiatan
                
                Data hanya digunakan untuk kepentingan program pemberdayaan desa.
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showTermsOfService() {
        AlertDialog.Builder(requireContext())
            .setTitle("Syarat & Ketentuan")
            .setMessage("""
                Dengan menggunakan aplikasi Bina Desa, Anda setuju untuk:
                
                1. Menggunakan data dengan bertanggung jawab
                2. Menjaga kerahasiaan akun
                3. Melaporkan jika ada data tidak sesuai
                4. Mendukung program pemberdayaan desa
            """.trimIndent())
            .setPositiveButton("Setuju", null)
            .show()
    }

    private fun showAboutApp() {
        AlertDialog.Builder(requireContext())
            .setTitle("Tentang Bina Desa")
            .setMessage("""
                Bina Desa adalah program pemberdayaan masyarakat desa yang bertujuan meningkatkan kesejahteraan warga desa melalui pembangunan dan pemberdayaan.
                
                Dikembangkan oleh: Tahnia
                NIM: 20220040080
                Teknik Informatika - Universitas Nusa Putra
                
                © 2024 Bina Desa. All Rights Reserved.
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showContactInfo() {
        AlertDialog.Builder(requireContext())
            .setTitle("Hubungi Kami")
            .setMessage("""
                Email: binadesa@gmail.com
                Telepon: (0266) 123456
                Website: https://tahnia-posyandu.alwaysdata.net
                
                Alamat: Jl. Raya Cikidang, Sukabumi
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}