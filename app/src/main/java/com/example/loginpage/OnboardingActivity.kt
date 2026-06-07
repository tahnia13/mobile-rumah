package com.example.loginpage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.loginpage.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf(
            OnboardingItem(
                "Sistem Informasi Posyandu",
                "Selamat datang di POSDES, solusi digital untuk pemantauan kesehatan ibu dan anak yang terintegrasi di tingkat desa.",
                R.drawable.posdes
            ),
            OnboardingItem(
                "Monitoring Kesehatan Balita",
                "Catat berat badan, tinggi badan, dan riwayat imunisasi secara digital untuk memastikan tumbuh kembang anak yang optimal.",
                R.drawable.monitoringbayi
            ),
            OnboardingItem(
                "Jadwal & Layanan Terpadu",
                "Dapatkan informasi jadwal kegiatan Posyandu dan akses laporan kesehatan secara transparan kapan saja.",
                R.drawable.jadwallayananterpadu
            )
        )

        val adapter = OnboardingAdapter(items)
        binding.viewPager.adapter = adapter

        // Setup indicator dots using WormDotsIndicator
        binding.dotIndicator.attachTo(binding.viewPager)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == items.size - 1) {
                    binding.btnNext.visibility = View.GONE
                    binding.btnGetStarted.visibility = View.VISIBLE
                } else {
                    binding.btnNext.visibility = View.VISIBLE
                    binding.btnGetStarted.visibility = View.GONE
                }
            }
        })

        binding.btnNext.setOnClickListener {
            binding.viewPager.currentItem = binding.viewPager.currentItem + 1
        }

        binding.btnGetStarted.setOnClickListener {
            // Pindah ke Login (MainActivity)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}