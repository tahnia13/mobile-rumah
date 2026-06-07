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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginpage.databinding.FragmentHomePosyanduBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        setupNews()
        loadPhoto()
    }

    private fun loadPhoto() {
        // Menggunakan foto yang relevan dengan tema Posyandu Desa
        val photos = listOf(
            PhotoModel("Pemeriksaan Balita", "https://loremflickr.com/400/300/baby,clinic?lock=1"),
            PhotoModel("Ibu & Anak", "https://loremflickr.com/400/300/mother,baby?lock=2"),
            PhotoModel("Kesehatan Desa", "https://loremflickr.com/400/300/health,doctor?lock=3"),
            PhotoModel("Imunisasi", "https://loremflickr.com/400/300/vaccine?lock=4"),
            PhotoModel("Gizi Anak", "https://loremflickr.com/400/300/nutrition,food?lock=5"),
            PhotoModel("Posyandu Rutin", "https://loremflickr.com/400/300/clinic,village?lock=6"),
            PhotoModel("Edukasi Gizi", "https://loremflickr.com/400/300/health,education?lock=7"),
            PhotoModel("Tumbuh Kembang", "https://loremflickr.com/400/300/baby,play?lock=8"),
            PhotoModel("Layanan Lansia", "https://loremflickr.com/400/300/senior,health?lock=9"),
            PhotoModel("Kader Aktif", "https://loremflickr.com/400/300/nurse,community?lock=10")
        )
        
        val adapter = PhotoAdapter(photos)
        binding.rvGallery.adapter = adapter
        binding.rvGallery.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupNews() {
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        fetchNews()
    }

    private fun fetchNews() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-berita-indonesia.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(NewsApiService::class.java)
        // Menggunakan kategori 'gaya-hidup' agar lebih relevan dengan kesehatan
        service.getLifestyle().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val posts = response.body()?.data?.posts ?: emptyList()
                    binding.rvNews.adapter = NewsAdapter(posts)
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Handle failure
            }
        })
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