package com.example.loginpage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.loginpage.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Posyandu Desa"

        // Di MainActivity2, tombol back tidak ditampilkan
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        // Cek apakah user sudah login
        if (!sharedPreferences.getBoolean("isLogin", false)) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Setup Bottom Navigation
        bottomNav = findViewById(R.id.bottomNavigation)

        // Load fragment default (Home Posyandu)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentHomePosyandu())
                .commit()
            supportActionBar?.title = "Posyandu Desa"
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        // Set listener untuk bottom navigation
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FragmentHomePosyandu())
                        .commit()
                    supportActionBar?.title = "Posyandu Desa"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    true
                }
                R.id.nav_bina_desa -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FragmentBinaDesa())
                        .commit()
                    supportActionBar?.title = "Bina Desa"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    true
                }
                R.id.nav_balita -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FragmentBalita())
                        .commit()
                    supportActionBar?.title = "Data Balita"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    true
                }
                R.id.nav_note -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FragmentNote())
                        .commit()
                    supportActionBar?.title = "Catatan Kesehatan"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    true
                }
                R.id.nav_about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FragmentAbout())
                        .commit()
                    supportActionBar?.title = "Tentang Posyandu"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    true
                }
                else -> false
            }
        }

        // Menggunakan OnBackPressedCallback (cara modern, tidak deprecated)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportActionBar?.title == "Posyandu Desa") {
                    // Jika di halaman utama, tutup aplikasi
                    finish()
                } else {
                    // Kembali ke Dashboard jika tidak di fragment Home
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, FragmentHomePosyandu())
                        .commit()
                    supportActionBar?.title = "Posyandu Desa"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    bottomNav.selectedItemId = R.id.nav_home
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Kembali ke fragment Home (Dashboard)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, FragmentHomePosyandu())
                    .commit()
                supportActionBar?.title = "Posyandu Desa"
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                // Set selected item di bottom navigation ke Home
                bottomNav.selectedItemId = R.id.nav_home
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}