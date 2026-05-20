package com.example.f1manager

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        setContentView(R.layout.activity_menu)

        findViewById<View>(R.id.btnJogar).setOnClickListener {
            Toast.makeText(this, "Abrindo jogo...", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnGaragem).setOnClickListener {
            Toast.makeText(this, "Abrindo garagem...", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnOpcoes).setOnClickListener {
            Toast.makeText(this, "Abrindo opções...", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnSair).setOnClickListener {
            finish()
        }
    }
}