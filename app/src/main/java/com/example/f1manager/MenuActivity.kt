package com.example.f1manager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
                    android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        setContentView(R.layout.activity_menu)

        val btnJogar = findViewById<View>(R.id.btnJogar)
        val btnGaragem = findViewById<View>(R.id.btnGaragem)
        val btnOpcoes = findViewById<View>(R.id.btnOpcoes)
        val btnSair = findViewById<View>(R.id.btnSair)

        btnJogar.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        btnGaragem.setOnClickListener {
            Toast.makeText(this, "Abrindo garagem...", Toast.LENGTH_SHORT).show()
        }

        btnOpcoes.setOnClickListener {
            Toast.makeText(this, "Abrindo opções...", Toast.LENGTH_SHORT).show()
        }

        btnSair.setOnClickListener {
            finish()
        }
    }
}