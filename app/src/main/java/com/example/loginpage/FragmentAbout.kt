package com.example.loginpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.loginpage.databinding.FragmentAboutBinding

class FragmentAbout : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set konten tentang Bina Desa
        binding.tvJudul.text = "📋 TENTANG BINA DESA"

        binding.tvDefinisi.text = """
            Bina Desa adalah program pemberdayaan masyarakat desa yang bertujuan untuk meningkatkan kualitas hidup dan kesejahteraan warga desa melalui berbagai kegiatan pembangunan dan pemberdayaan.
        """.trimIndent()

        binding.tvFitur.text = """
            ✨ FITUR-FITUR BINA DESA:
            
            • 📊 Dashboard Monitoring - Memantau perkembangan program desa secara real-time
            • 👥 Data Penduduk - Mengelola data kependudukan desa dengan mudah
            • 💰 Laporan Keuangan - Transparansi pengelolaan dana desa
            • 🏥 Layanan Kesehatan - Informasi layanan posyandu dan kesehatan
            • 📈 Program Pembangunan - Tracking proyek pembangunan desa
            • 🌾 UMKM Desa - Pemasaran produk unggulan desa
            • 📚 Edukasi Masyarakat - Pelatihan dan penyuluhan warga
            • 🔄 Kolaborasi - Sinergi antar desa dan stakeholder
        """.trimIndent()

        binding.tvTujuan.text = """
            🎯 TUJUAN BINA DESA:
            
            1. Meningkatkan kesejahteraan masyarakat desa
            2. Mempercepat pembangunan infrastruktur desa
            3. Mengembangkan potensi dan sumber daya lokal
            4. Mewujudkan tata kelola pemerintahan desa yang baik
            5. Meningkatkan partisipasi aktif masyarakat dalam pembangunan
        """.trimIndent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}