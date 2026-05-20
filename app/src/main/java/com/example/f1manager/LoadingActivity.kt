package com.example.f1manager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {

    private var progresso = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val progressBar = findViewById<ProgressBar>(R.id.progressLoading)
        val txtPorcentagem = findViewById<TextView>(R.id.txtPorcentagem)

        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                progresso += 2
                progressBar.progress = progresso
                txtPorcentagem.text = "$progresso%"

                if (progresso < 100) {
                    handler.postDelayed(this, 40)
                } else {
                    startActivity(Intent(this@LoadingActivity, MenuActivity::class.java))
                    finish()
                }
            }
        }

        handler.post(runnable)
    }
}