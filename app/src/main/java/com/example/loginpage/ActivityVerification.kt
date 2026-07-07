package com.example.loginpage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class ActivityVerification : AppCompatActivity() {

    private lateinit var otp1: EditText
    private lateinit var otp2: EditText
    private lateinit var otp3: EditText
    private lateinit var otp4: EditText
    private lateinit var otp5: EditText
    private lateinit var otp6: EditText
    private lateinit var btnVerify: Button
    private lateinit var btnResend: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvNoHp: TextView

    private lateinit var sharedPreferences: SharedPreferences
    private var username: String = ""
    private var noHp: String = ""
    private var correctOTP: String = ""
    private var timer: CountDownTimer? = null
    private var isResendEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        // Inisialisasi view
        otp1 = findViewById(R.id.otp1)
        otp2 = findViewById(R.id.otp2)
        otp3 = findViewById(R.id.otp3)
        otp4 = findViewById(R.id.otp4)
        otp5 = findViewById(R.id.otp5)
        otp6 = findViewById(R.id.otp6)
        btnVerify = findViewById(R.id.btnVerify)
        btnResend = findViewById(R.id.btnResend)
        tvTimer = findViewById(R.id.tvTimer)
        tvNoHp = findViewById(R.id.tvNoHp)

        // SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Ambil data
        username = intent.getStringExtra("username") ?: ""
        noHp = intent.getStringExtra("no_hp") ?: ""
        correctOTP = sharedPreferences.getString("temp_otp", "") ?: ""

        // Tampilkan pesan
        tvNoHp.text = "Kode verifikasi dikirim ke $noHp"

        // Setup auto pindah
        setupAutoMove()

        // Tombol verifikasi
        btnVerify.setOnClickListener {
            verifyOTP()
        }

        // Tombol kirim ulang
        btnResend.setOnClickListener {
            if (isResendEnabled) {
                resendOTP()
            } else {
                Toast.makeText(this, "Tunggu ${tvTimer.text} detik", Toast.LENGTH_SHORT).show()
            }
        }

        // Mulai timer
        startTimer()
    }

    private fun setupAutoMove() {
        // OTP 1 ke 2
        otp1.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) otp2.requestFocus()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // OTP 2 ke 3 atau 1
        otp2.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) otp3.requestFocus()
                else if (s?.length == 0) otp1.requestFocus()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // OTP 3 ke 4 atau 2
        otp3.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) otp4.requestFocus()
                else if (s?.length == 0) otp2.requestFocus()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // OTP 4 ke 5 atau 3
        otp4.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) otp5.requestFocus()
                else if (s?.length == 0) otp3.requestFocus()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // OTP 5 ke 6 atau 4
        otp5.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) otp6.requestFocus()
                else if (s?.length == 0) otp4.requestFocus()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // OTP 6 ke 5
        otp6.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0) otp5.requestFocus()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun getOTP(): String {
        return otp1.text.toString() + otp2.text.toString() + otp3.text.toString() +
                otp4.text.toString() + otp5.text.toString() + otp6.text.toString()
    }

    private fun clearOTP() {
        otp1.setText("")
        otp2.setText("")
        otp3.setText("")
        otp4.setText("")
        otp5.setText("")
        otp6.setText("")
        otp1.requestFocus()
    }

    private fun verifyOTP() {
        val enteredOTP = getOTP()

        if (enteredOTP.length < 6) {
            Toast.makeText(this, "Masukkan 6 digit kode OTP", Toast.LENGTH_SHORT).show()
            return
        }

        if (enteredOTP == correctOTP) {
            // Simpan data user
            val editor = sharedPreferences.edit()
            val nama = sharedPreferences.getString("temp_nama", "") ?: ""
            val username = sharedPreferences.getString("temp_username", "") ?: ""
            val password = sharedPreferences.getString("temp_password", "") ?: ""

            editor.putString("user_$username", "$nama|$noHp|$password")
            editor.putString("username_$username", username)
            editor.remove("temp_nama")
            editor.remove("temp_noHp")
            editor.remove("temp_username")
            editor.remove("temp_password")
            editor.remove("temp_otp")
            editor.apply()

            Toast.makeText(this, "Verifikasi berhasil! Silakan login", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Kode OTP salah!", Toast.LENGTH_SHORT).show()
            clearOTP()
        }
    }

    private fun startTimer() {
        isResendEnabled = false
        btnResend.isEnabled = false
        btnResend.alpha = 0.5f

        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                isResendEnabled = true
                btnResend.isEnabled = true
                btnResend.alpha = 1.0f
                tvTimer.text = "0"
                Toast.makeText(this@ActivityVerification, "Anda dapat mengirim ulang kode", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun resendOTP() {
        // Gunakan 6 angka terakhir dari No. HP sebagai OTP
        val newOTP = if (noHp.length >= 6) {
            noHp.substring(noHp.length - 6)
        } else {
            noHp
        }
        correctOTP = newOTP

        // Simpan OTP baru
        val editor = sharedPreferences.edit()
        editor.putString("temp_otp", newOTP)
        editor.apply()

        Toast.makeText(this, "Kode OTP: $newOTP (6 angka terakhir No. HP)", Toast.LENGTH_LONG).show()

        // Reset timer
        timer?.cancel()
        startTimer()
        clearOTP()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}