package com.example.loginpage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginpage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        // Cek apakah user SUDAH LOGIN sebelumnya (isLogin = true)
        if (sharedPreferences.getBoolean("isLogin", false)) {
            // Jika sudah login, langsung ke dashboard
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }

        // Link ke Register
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Validasi input kosong
            if (email.isEmpty()) {
                binding.etEmail.error = "Masukkan email/username"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Masukkan password"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            // Coba login
            if (performLogin(email, password)) {
                // Login berhasil - simpan session
                saveLoginSession(email, password)

                Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                finish()
            } else {
                // Login gagal
                Toast.makeText(this, "Email/username atau password salah!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Melakukan pengecekan login dengan 2 rule:
     * 1. Username == Password
     * 2. Username & Password sesuai dengan data di SharedPreferences
     */
    private fun performLogin(username: String, password: String): Boolean {
        // Rule 1: Username == Password
        if (username == password) {
            return true
        }

        // Rule 2: Cek dengan data di SharedPreferences (hasil registrasi)
        val savedUsername = sharedPreferences.getString("userEmail", "")
        val savedPassword = sharedPreferences.getString("password", "")

        // Jika username dan password cocok dengan data yang tersimpan
        if (username == savedUsername && password == savedPassword) {
            return true
        }

        // Jika tidak memenuhi kedua rule, login gagal
        return false
    }

    /**
     * Menyimpan session login ke SharedPreferences
     */
    private fun saveLoginSession(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLogin", true)
        editor.putString("userEmail", username)

        // Jika login dengan rule 1, simpan username sebagai nama
        if (username == password) {
            editor.putString("userName", username)
        }
        // Jika login dengan data registrasi, nama sudah tersimpan dari registrasi

        editor.apply()
    }
}