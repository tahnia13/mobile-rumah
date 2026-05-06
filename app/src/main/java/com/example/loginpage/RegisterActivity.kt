package com.example.loginpage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginpage.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol Register
        binding.btnRegister.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val noHp = binding.etNoHp.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Validasi input
            if (nama.isEmpty()) {
                binding.etNama.error = "Masukkan nama lengkap"
                binding.etNama.requestFocus()
                return@setOnClickListener
            }

            if (noHp.isEmpty()) {
                binding.etNoHp.error = "Masukkan nomor handphone"
                binding.etNoHp.requestFocus()
                return@setOnClickListener
            }

            if (noHp.length < 10) {
                binding.etNoHp.error = "Nomor handphone minimal 10 digit"
                binding.etNoHp.requestFocus()
                return@setOnClickListener
            }

            if (username.isEmpty()) {
                binding.etUsername.error = "Masukkan username"
                binding.etUsername.requestFocus()
                return@setOnClickListener
            }

            if (username.length < 4) {
                binding.etUsername.error = "Username minimal 4 karakter"
                binding.etUsername.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Masukkan password"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.etPassword.error = "Password minimal 6 karakter"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            // Generate OTP (6 digit random)
            val otp = generateOTP()

            // Pindah ke halaman verifikasi
            val intent = Intent(this, VerifyOtpActivity::class.java)
            intent.putExtra("nama", nama)
            intent.putExtra("noHp", noHp)
            intent.putExtra("username", username)
            intent.putExtra("password", password)
            intent.putExtra("otp", otp)
            startActivity(intent)
        }

        // Link ke Login
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun generateOTP(): String {
        val random = java.util.Random()
        val otp = 100000 + random.nextInt(900000)
        return otp.toString()
    }
}