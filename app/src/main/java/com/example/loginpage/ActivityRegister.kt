package com.example.loginpage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginpage.databinding.ActivityRegisterBinding
import java.util.Random

class ActivityRegister : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var generatedOTP: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Registrasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        binding.btnRegister.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val noHp = binding.etNoHp.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Validasi input
            when {
                nama.isEmpty() -> {
                    binding.etNama.error = "Nama tidak boleh kosong"
                    binding.etNama.requestFocus()
                    return@setOnClickListener
                }
                noHp.isEmpty() -> {
                    binding.etNoHp.error = "No. Handphone tidak boleh kosong"
                    binding.etNoHp.requestFocus()
                    return@setOnClickListener
                }
                noHp.length < 10 -> {
                    binding.etNoHp.error = "No. Handphone minimal 10 digit"
                    binding.etNoHp.requestFocus()
                    return@setOnClickListener
                }
                username.isEmpty() -> {
                    binding.etUsername.error = "Username tidak boleh kosong"
                    binding.etUsername.requestFocus()
                    return@setOnClickListener
                }
                username.length < 4 -> {
                    binding.etUsername.error = "Username minimal 4 karakter"
                    binding.etUsername.requestFocus()
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    binding.etPassword.error = "Password tidak boleh kosong"
                    binding.etPassword.requestFocus()
                    return@setOnClickListener
                }
                password.length < 6 -> {
                    binding.etPassword.error = "Password minimal 6 karakter"
                    binding.etPassword.requestFocus()
                    return@setOnClickListener
                }
            }

            // Cek apakah username sudah terdaftar
            val existingUser = sharedPreferences.getString("username_$username", null)
            if (existingUser != null) {
                Toast.makeText(this, "Username sudah terdaftar!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil 6 angka terakhir dari No. HP sebagai OTP
            generatedOTP = if (noHp.length >= 6) {
                noHp.substring(noHp.length - 6)
            } else {
                noHp // Fallback jika < 6 digit (sudah divalidasi minimal 10)
            }

            // Simpan data user sementara
            val editor = sharedPreferences.edit()
            editor.putString("temp_nama", nama)
            editor.putString("temp_noHp", noHp)
            editor.putString("temp_username", username)
            editor.putString("temp_password", password)
            editor.putString("temp_otp", generatedOTP)
            editor.apply()

            // Simulasi pengiriman OTP (Toast notifikasi)
            Toast.makeText(this, "Kode OTP: $generatedOTP", Toast.LENGTH_LONG).show()

            // Arahkan ke halaman verifikasi
            val intent = Intent(this, ActivityVerification::class.java)
            intent.putExtra("username", username)
            intent.putExtra("no_hp", noHp)
            startActivity(intent)
        }

        // Link ke halaman login
        binding.tvLoginHere.setOnClickListener {
        finish()
    }
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}