package com.example.loginpage

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class RumusBangunRuangActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rumus_bangun_ruang)

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Rumus Bangun Ruang"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val tvRumusKonten = findViewById<TextView>(R.id.tvRumusKonten)

        tvRumusKonten.text = """
            ★ KUBUS ★
            Volume = s × s × s
            Luas Permukaan = 6 × s²
            
            ★ BALOK ★
            Volume = p × l × t
            Luas Permukaan = 2 × (pl + pt + lt)
            
            ★ BOLA ★
            Volume = 4/3 × π × r³
            Luas Permukaan = 4 × π × r²
            
            ★ TABUNG ★
            Volume = π × r² × t
            Luas Permukaan = 2 × π × r × (r + t)
        """.trimIndent()
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