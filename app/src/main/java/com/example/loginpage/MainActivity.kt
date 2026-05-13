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

        // Cek apakah user sudah login sebelumnya
        if (sharedPreferences.getBoolean("isLogin", false)) {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
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

            // Cek login (untuk user registrasi)
            val userDataPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
            val savedUserData = userDataPref.getString("user_$email", null)

            if (savedUserData != null) {
                val parts = savedUserData.split("|")
                val savedPassword = if (parts.size > 2) parts[2] else ""

                if (password == savedPassword) {
                    // Login berhasil untuk user registrasi
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLogin", true)
                    editor.putString("userEmail", email)
                    editor.putString("userName", parts[0])
                    editor.apply()

                    Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Password salah!", Toast.LENGTH_SHORT).show()
                }
            }
            // Login untuk user default (admin)
            else if (email == "tahnia@gmail.com" && password == "tahnia123") {
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("userEmail", email)
                editor.putString("userName", "Admin Posyandu")
                editor.apply()

                Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email/Username atau password salah!", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Register
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, ActivityRegister::class.java)
            startActivity(intent)
        }
    }
}