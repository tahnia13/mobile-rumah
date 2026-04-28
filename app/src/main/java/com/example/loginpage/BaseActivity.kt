package com.example.loginpage

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var tvJudulHalaman: TextView
    protected lateinit var tvDeskripsiHalaman: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val judul = intent.getStringExtra("judul_halaman")
        val deskripsi = intent.getStringExtra("deskripsi")

        supportActionBar?.title = judul ?: defaultTitle
    }

    protected fun setupHeader(judulTextViewId: Int, deskripsiTextViewId: Int) {
        tvJudulHalaman = findViewById(judulTextViewId)
        tvDeskripsiHalaman = findViewById(deskripsiTextViewId)

        val judul = intent.getStringExtra("judul_halaman")
        val deskripsi = intent.getStringExtra("deskripsi")

        tvJudulHalaman.text = judul ?: defaultTitle
        tvDeskripsiHalaman.text = deskripsi ?: defaultDescription
    }

    protected open val defaultTitle: String = "Halaman"
    protected open val defaultDescription: String = "Deskripsi halaman"
}