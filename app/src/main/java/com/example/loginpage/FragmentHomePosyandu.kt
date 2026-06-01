package com.example.loginpage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.loginpage.databinding.FragmentHomePosyanduBinding
import com.google.android.material.chip.Chip

class FragmentHomePosyandu : Fragment() {

    private var _binding: FragmentHomePosyanduBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePosyanduBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi SharedPreferences untuk menyimpan data balita
        sharedPref = requireContext().getSharedPreferences("DataBalita", Context.MODE_PRIVATE)

        // Ambil data user
        val userSession = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userName = userSession.getString("userName", "User")
        binding.tvUserName.text = "Halo, $userName!"

        // Load data balita yang tersimpan
        loadDataBalita()

        setupLayananChips()
        setupTombolLama()
        setupClickListeners()
    }

    private fun loadDataBalita() {
        // Load data dari SharedPreferences
        val namaBalita = sharedPref.getString("nama_balita", "")
        val usiaBalita = sharedPref.getString("usia_balita", "")
        val beratBadan = sharedPref.getString("berat_badan", "")
        val tinggiBadan = sharedPref.getString("tinggi_badan", "")

        binding.etNamaBalita.setText(namaBalita)
        binding.etUsiaBalita.setText(usiaBalita)
        binding.etBeratBadan.setText(beratBadan)
        binding.etTinggiBadan.setText(tinggiBadan)
    }

    private fun saveDataBalita() {
        val namaBalita = binding.etNamaBalita.text.toString().trim()
        val usiaBalita = binding.etUsiaBalita.text.toString().trim()
        val beratBadan = binding.etBeratBadan.text.toString().trim()
        val tinggiBadan = binding.etTinggiBadan.text.toString().trim()

        // Validasi input
        if (namaBalita.isEmpty()) {
            binding.etNamaBalita.error = "Nama balita tidak boleh kosong"
            binding.etNamaBalita.requestFocus()
            return
        }

        if (usiaBalita.isEmpty()) {
            binding.etUsiaBalita.error = "Usia balita tidak boleh kosong"
            binding.etUsiaBalita.requestFocus()
            return
        }

        if (beratBadan.isEmpty()) {
            binding.etBeratBadan.error = "Berat badan tidak boleh kosong"
            binding.etBeratBadan.requestFocus()
            return
        }

        if (tinggiBadan.isEmpty()) {
            binding.etTinggiBadan.error = "Tinggi badan tidak boleh kosong"
            binding.etTinggiBadan.requestFocus()
            return
        }

        // Simpan ke SharedPreferences
        val editor = sharedPref.edit()
        editor.putString("nama_balita", namaBalita)
        editor.putString("usia_balita", usiaBalita)
        editor.putString("berat_badan", beratBadan)
        editor.putString("tinggi_badan", tinggiBadan)
        editor.apply()

        Toast.makeText(requireContext(), "Data balita berhasil disimpan!", Toast.LENGTH_SHORT).show()
    }

    private fun setupLayananChips() {
        val layananList = listOf(
            "📊 Data Posyandu",
            "👶 Balita & Ibu",
            "📅 Jadwal Kegiatan",
            "💉 Imunisasi",
            "📊 Laporan",
            "👤 Admin"
        )

        for (layanan in layananList) {
            val chip = Chip(requireContext()).apply {
                text = layanan
                isClickable = true
                setChipBackgroundColorResource(android.R.color.transparent)
                setTextColor(resources.getColor(R.color.teal_700, null))

                setOnClickListener {
                    Toast.makeText(requireContext(), "Membuka: $layanan", Toast.LENGTH_SHORT).show()
                }
            }
            binding.chipGroupLayanan.addView(chip)
        }
    }

    private fun setupTombolLama() {
        // Tombol Rumus Bangun Ruang
        binding.btnRuang.setOnClickListener {
            val intent = Intent(requireContext(), RumusBangunRuangActivity::class.java)
            startActivity(intent)
        }

        // Tombol Buka Dashboard Web
        binding.btnWebDashboard.setOnClickListener {
            val url = "https://tahnia-posyandu.alwaysdata.net/dashboard"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        // Tombol Custom Page 1
        binding.btnCustom1.setOnClickListener {
            val intent = Intent(requireContext(), Custom1Activity::class.java)
            startActivity(intent)
        }

        // Tombol Custom Page 2
        binding.btnCustom2.setOnClickListener {
            val intent = Intent(requireContext(), Custom2Activity::class.java)
            startActivity(intent)
        }
    }

    private fun setupClickListeners() {
        // Tombol Simpan Data Balita
        binding.btnSimpanData.setOnClickListener {
            saveDataBalita()
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Ya") { _, _ ->
                val sharedPreferences = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()

                Toast.makeText(requireContext(), "Logout berhasil!", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}