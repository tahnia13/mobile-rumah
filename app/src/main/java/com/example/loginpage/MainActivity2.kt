package com.example.loginpage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.Manifest
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.loginpage.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Posyandu Desa"

        // Di MainActivity2, tombol back tidak ditampilkan
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        // Request Permission Notifikasi (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

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
            handleNavigation(menuItem.itemId)
        }

        // Handle intent dari notifikasi
        handleIntent(intent)

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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val fragmentTag = intent?.getStringExtra("open_fragment")
        if (fragmentTag != null) {
            when (fragmentTag) {
                "BALITA" -> {
                    handleNavigation(R.id.nav_balita)
                    bottomNav.selectedItemId = R.id.nav_balita
                }
                "NOTE" -> {
                    handleNavigation(R.id.nav_note)
                    bottomNav.selectedItemId = R.id.nav_note
                }
            }
        }
    }

    private fun handleNavigation(itemId: Int): Boolean {
        return when (itemId) {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.queryHint = "Cari informasi..."
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val targetId = when (query.lowercase().trim()) {
                        "balita", "data balita", "anak", "bayi" -> R.id.nav_balita
                        "note", "catatan", "kesehatan", "sakit", "jurnal" -> R.id.nav_note
                        "desa", "bina desa", "unit", "kader", "warga" -> R.id.nav_bina_desa
                        "tentang", "about", "info", "posyandu" -> R.id.nav_about
                        "home", "beranda", "utama", "dashboard" -> R.id.nav_home
                        else -> null
                    }

                    if (targetId != null) {
                        handleNavigation(targetId)
                        bottomNav.selectedItemId = targetId
                        searchItem?.collapseActionView()
                    } else {
                        Toast.makeText(this@MainActivity2, "Pencarian '$query' tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Bisa ditambahkan logika filter di sini nanti
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                // Secara default, SearchView akan menangani klik pada ikon search
                true
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
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