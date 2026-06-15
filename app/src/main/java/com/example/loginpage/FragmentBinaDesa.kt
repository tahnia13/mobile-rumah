package com.example.loginpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.loginpage.data.model.PosyanduItem
import com.example.loginpage.databinding.FragmentBinaDesaBinding
import com.google.android.material.tabs.TabLayout

class FragmentBinaDesa : Fragment() {

    private var _binding: FragmentBinaDesaBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PosyanduAdapter
    private val allItems = mutableListOf<PosyanduItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBinaDesaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupRecyclerView()
        setupTabLayout()
        
        // Default filter ke tab 'Warga' (index 0)
        binding.tabLayout.getTabAt(0)?.select()
        filterData("Warga")
    }

    private fun setupData() {
        allItems.clear()
        
        // --- WARGA (10 Items) ---
        val names = listOf("Budi Santoso", "Ani Wijaya", "Citra Lestari", "Dedi Kurniawan", "Eka Putri", "Fajar Ramadhan", "Gita Permata", "Hadi Saputra", "Indah Sari", "Joko Susilo")
        for (i in names.indices) {
            allItems.add(PosyanduItem(names[i], "NIK: 320102140590000${i+1}", "WARGA", "Jenis Kelamin: ${if(i%2==0) "Laki-laki" else "Perempuan"}", "Status: Aktif", "Alamat: RT 0${(i%5)+1} RW 01", "Warga", "https://picsum.photos/seed/warga$i/200"))
        }

        // --- USER (10 Items) ---
        for (i in 1..10) {
            allItems.add(PosyanduItem("User Sistem $i", "user$i@posdes.id", "ADMIN", "Status: Aktif", "Akses: Terbatas", "Last Login: 2023", "User", "https://picsum.photos/seed/user$i/200"))
        }

        // --- POSYANDU (10 Items) ---
        val posyanduNames = listOf("Mawar", "Melati", "Kenanga", "Anggrek", "Bougainville", "Cempaka", "Tulip", "Flamboyan", "Kamboja", "Teratai")
        for (i in posyanduNames.indices) {
            allItems.add(PosyanduItem("Posyandu ${posyanduNames[i]}", "Dusun ${if(i<5) "Utara" else "Selatan"}", "UNIT ${i+1}", "Ketua: Ibu Kader $i", "Jadwal: Minggu ke-${(i%4)+1}", "Kecamatan Banjar", "Posyandu", "https://picsum.photos/seed/posyandu$i/200"))
        }

        // --- KADER (10 Items) ---
        for (i in 1..10) {
            allItems.add(PosyanduItem("Kader Posyandu $i", "Sertifikat Nasional", "AKTIF", "Spesialis: Gizi & KIA", "Masa Bakti: 2021-2026", "Pelatihan: Dasar", "Kader", "https://picsum.photos/seed/kader$i/200"))
        }

        // --- JADWAL (10 Items) ---
        val kegiatan = listOf("Imunisasi Rutin", "Penimbangan Balita", "Edukasi Gizi", "Pemeriksaan Lansia", "Kelas Ibu Hamil", "Senam Sehat", "Pemberian Vitamin", "Cek HB Remaja", "Fogging Nyamuk", "Penyuluhan KB")
        for (i in kegiatan.indices) {
            allItems.add(PosyanduItem(kegiatan[i], "Lokasi: Balai Desa", "JADWAL", "Tanggal: ${i+10} Okt 2023", "Pukul: 08:00 WIB", "Status: Terjadwal", "Jadwal", "https://picsum.photos/seed/jadwal$i/200"))
        }

        // --- LAYANAN (10 Items) ---
        val layananList = listOf("Pemeriksaan Balita", "Konsultasi Ibu Hamil", "Pemberian Vitamin A", "Cek Hemoglobin (HB)", "Imunisasi Dasar", "Konsultasi KB", "Penyuluhan Gizi", "Posbindu PTM", "Senam Ibu Hamil", "Kelas Parenting")
        for (i in layananList.indices) {
            allItems.add(PosyanduItem(layananList[i], "Layanan Terpadu", "LAYANAN", "Gratis untuk Warga", "Setiap Hari Kerja", "Fasilitas Lengkap", "Layanan", "https://picsum.photos/seed/layanan$i/200"))
        }

        // --- IMUNISASI (10 Items) ---
        val imunisasiList = listOf("Hepatitis B", "BCG (TBC)", "Polio Tetes 1", "DPT-HB-Hib 1", "Polio Tetes 2", "DPT-HB-Hib 2", "Polio Tetes 3", "DPT-HB-Hib 3", "Campak / MR", "Polio Suntik (IPV)")
        for (i in imunisasiList.indices) {
            allItems.add(PosyanduItem(imunisasiList[i], "Wajib bagi Bayi", "IMUNISASI", "Usia Sasaran: ${(i*2)} Bulan", "Dosis: ${(i%2)+1} Kali", "Catatan: Bawa Buku KIA", "Imunisasi", "https://picsum.photos/seed/imunisasi$i/200"))
        }
    }

    private fun setupRecyclerView() {
        adapter = PosyanduAdapter(mutableListOf()) { selectedItem ->
            Toast.makeText(requireContext(), "Membuka: ${selectedItem.title}", Toast.LENGTH_SHORT).show()
        }
        
        binding.rvBinaDesa.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = this@FragmentBinaDesa.adapter
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = tab?.text.toString()
                filterData(category)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun filterData(category: String) {
        val filteredList = allItems.filter { it.category == category }
        adapter.updateData(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}