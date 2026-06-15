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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginpage.data.AppDatabase
import com.example.loginpage.data.api.NewsApiService
import com.example.loginpage.data.entity.BalitaEntity
import com.example.loginpage.data.entity.CatatanEntity
import com.example.loginpage.data.model.NewsResponse
import com.example.loginpage.data.model.PhotoModel
import com.example.loginpage.databinding.FragmentHomePosyanduBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FragmentHomePosyandu : Fragment() {

    private var _binding: FragmentHomePosyanduBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPreferences
    private lateinit var database: AppDatabase
    private lateinit var catatanAdapter: CatatanAdapter
    private var currentBalita: BalitaEntity? = null

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

        // Inisialisasi Database Room
        database = AppDatabase.getDatabase(requireContext())

        // Inisialisasi SharedPreferences
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
        setupRecyclerViewCatatan()
        observeCatatan()
    }

    private fun setupRecyclerViewCatatan() {
        catatanAdapter = CatatanAdapter(emptyList()) { catatan ->
            lifecycleScope.launch(Dispatchers.IO) {
                database.posyanduDao().deleteCatatan(catatan)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Catatan dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.rvCatatan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = catatanAdapter
        }
    }

    private fun observeCatatan() {
        lifecycleScope.launch {
            database.posyanduDao().getAllCatatan().collectLatest { list ->
                catatanAdapter.updateData(list)
            }
        }
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
        lifecycleScope.launch {
            database.posyanduDao().getAllBalita().collectLatest { list ->
                if (list.isNotEmpty()) {
                    val balita = list[0] // Ambil data terakhir yang disimpan
                    currentBalita = balita
                    binding.etNamaBalita.setText(balita.nama)
                    binding.etUsiaBalita.setText(balita.usia)
                    binding.etBeratBadan.setText(balita.berat)
                    binding.etTinggiBadan.setText(balita.tinggi)
                }
            }
        }
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

        // Simpan ke Room Database
        lifecycleScope.launch(Dispatchers.IO) {
            val balita = BalitaEntity(
                id = currentBalita?.id ?: 0,
                nama = namaBalita,
                usia = usiaBalita,
                berat = beratBadan,
                tinggi = tinggiBadan
            )
            
            if (currentBalita == null) {
                database.posyanduDao().insertBalita(balita)
            } else {
                database.posyanduDao().updateBalita(balita)
            }
            
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Data balita berhasil disimpan (Room)!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCatatan() {
        val judul = binding.etJudulCatatan.text.toString().trim()
        val isi = binding.etIsiCatatan.text.toString().trim()

        if (judul.isEmpty() || isi.isEmpty()) {
            Toast.makeText(requireContext(), "Judul dan isi catatan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val tanggal = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date())

        lifecycleScope.launch(Dispatchers.IO) {
            val catatan = CatatanEntity(judul = judul, isi = isi, tanggal = tanggal)
            database.posyanduDao().insertCatatan(catatan)
            
            withContext(Dispatchers.Main) {
                binding.etJudulCatatan.text?.clear()
                binding.etIsiCatatan.text?.clear()
                Toast.makeText(requireContext(), "Catatan ditambahkan!", Toast.LENGTH_SHORT).show()
            }
        }
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
        // Tombol Profil
        binding.btnProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentProfile())
                .addToBackStack(null)
                .commit()
            (activity as? MainActivity2)?.apply {
                supportActionBar?.title = "Profil Developer"
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }

        // Tombol Pengaturan
        binding.btnSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentSettings())
                .addToBackStack(null)
                .commit()
            (activity as? MainActivity2)?.apply {
                supportActionBar?.title = "Pengaturan"
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }

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

        // Tombol Simpan Catatan
        binding.btnSimpanCatatan.setOnClickListener {
            saveCatatan()
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