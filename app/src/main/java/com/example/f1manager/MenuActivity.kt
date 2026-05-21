package com.example.f1manager

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_menu)

        val btnJogar = findViewById<View>(R.id.btnJogar)
        val btnGaragem = findViewById<View>(R.id.btnGaragem)
        val btnOpcoes = findViewById<View>(R.id.btnOpcoes)
        val btnSair = findViewById<View>(R.id.btnSair)

        btnJogar.setOnClickListener {
            Toast.makeText(this, "Entrando na corrida...", Toast.LENGTH_SHORT).show()
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