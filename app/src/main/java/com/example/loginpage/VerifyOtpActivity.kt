package com.example.loginpage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginpage.databinding.ActivityVerifyOtpBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class VerifyOtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyOtpBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var nama: String = ""
    private var noHp: String = ""
    private var username: String = ""
    private var password: String = ""
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 30000 // 30 detik
    private var expectedOtp: String = "" // 6 digit terakhir no HP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        // Ambil data dari intent
        nama = intent.getStringExtra("nama") ?: ""
        noHp = intent.getStringExtra("noHp") ?: ""
        username = intent.getStringExtra("username") ?: ""
        password = intent.getStringExtra("password") ?: ""

        // Ambil 6 digit terakhir dari nomor HP sebagai OTP
        expectedOtp = getLast6Digits(noHp)

        // Tampilkan info nomor HP
        binding.tvInfoNoHp.text = "Masukkan 6 digit terakhir dari nomor\n$noHp"

        // Tampilkan OTP untuk development (hanya di Toast)
        Toast.makeText(this, "OTP (6 digit terakhir): $expectedOtp", Toast.LENGTH_LONG).show()

        setupOtpInputs()
        startTimer()

        // Tombol Verifikasi
        binding.btnVerify.setOnClickListener {
            verifyOtp()
        }

        // Resend OTP
        binding.tvResend.setOnClickListener {
            resendOtp()
        }
    }

    private fun getLast6Digits(phoneNumber: String): String {
        val digitsOnly = phoneNumber.replace(Regex("[^0-9]"), "")

        return if (digitsOnly.length >= 6) {
            digitsOnly.substring(digitsOnly.length - 6)
        } else {
            digitsOnly.padStart(6, '0')
        }
    }

    private fun setupOtpInputs() {
        val otpFields = arrayOf(
            binding.etOtp1, binding.etOtp2, binding.etOtp3,
            binding.etOtp4, binding.etOtp5, binding.etOtp6
        )

        for (i in otpFields.indices) {
            otpFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < otpFields.size - 1) {
                        otpFields[i + 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun verifyOtp() {
        val enteredOtp = StringBuilder().apply {
            append(binding.etOtp1.text.toString())
            append(binding.etOtp2.text.toString())
            append(binding.etOtp3.text.toString())
            append(binding.etOtp4.text.toString())
            append(binding.etOtp5.text.toString())
            append(binding.etOtp6.text.toString())
        }.toString()

        if (enteredOtp.isEmpty() || enteredOtp.length < 6) {
            showErrorDialog(
                "Kode OTP Tidak Lengkap",
                "Mohon masukkan 6 digit kode OTP dengan lengkap."
            )
            return
        }

        if (enteredOtp == expectedOtp) {
            // Simpan data user ke SharedPreferences (TANPA auto login)
            saveUserData()
            showSuccessAndBackToLogin()
        } else {
            showErrorDialog(
                "Kode OTP Salah",
                "Kode OTP yang Anda masukkan tidak sesuai.\n\nSilakan periksa kembali dan masukkan 6 digit terakhir dari nomor handphone yang didaftarkan."
            )
            clearOtpFields()
        }
    }

    /**
     * Menyimpan data user hasil registrasi ke SharedPreferences
     * TAPI TIDAK langsung login (isLogin = false)
     */
    private fun saveUserData() {
        val editor = sharedPreferences.edit()
        // Simpan data user tapi JANGAN set isLogin = true
        // Biarkan user login manual setelah registrasi
        editor.putString("userName", nama)
        editor.putString("userEmail", username)
        editor.putString("userPhone", noHp)
        editor.putString("password", password)
        // Jangan set isLogin = true, biarkan user login sendiri
        editor.apply()
    }

    private fun showErrorDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Coba Lagi") { dialog, _ ->
                dialog.dismiss()
                binding.etOtp1.requestFocus()
            }
            .setCancelable(false)
            .show()
    }

    private fun showSuccessAndBackToLogin() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Registrasi Berhasil! ✅")
            .setMessage("Selamat $nama!\n\nAkun Anda telah berhasil didaftarkan.\n\nSilakan login menggunakan username dan password yang telah didaftarkan.")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("Ke Halaman Login") { _, _ ->
                // Kembali ke halaman login (MainActivity)
                val intent = Intent(this, MainActivity::class.java)
                // Clear semua activity sebelumnya
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

                // Tampilkan toast di halaman login
                Toast.makeText(this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_LONG).show()
            }
            .setCancelable(false)
            .show()
    }

    private fun clearOtpFields() {
        binding.etOtp1.text?.clear()
        binding.etOtp2.text?.clear()
        binding.etOtp3.text?.clear()
        binding.etOtp4.text?.clear()
        binding.etOtp5.text?.clear()
        binding.etOtp6.text?.clear()
        binding.etOtp1.requestFocus()
    }

    private fun resendOtp() {
        countDownTimer?.cancel()
        timeLeftInMillis = 30000
        startTimer()
        binding.tvResend.isEnabled = false

        clearOtpFields()

        MaterialAlertDialogBuilder(this)
            .setTitle("Kode OTP Dikirim Ulang")
            .setMessage("Kode OTP telah dikirim ulang ke nomor:\n$noHp\n\nSilakan masukkan 6 digit terakhir dari nomor handphone Anda.")
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                binding.etOtp1.requestFocus()
            }
            .show()
    }

    private fun startTimer() {
        binding.tvResend.isEnabled = false
        binding.tvResend.setTextColor(resources.getColor(android.R.color.darker_gray, theme))

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvTimer.text = String.format("00:%02d", seconds)
            }

            override fun onFinish() {
                binding.tvTimer.text = "00:00"
                binding.tvResend.isEnabled = true
                binding.tvResend.setTextColor(resources.getColor(R.color.teal_700, theme))
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}