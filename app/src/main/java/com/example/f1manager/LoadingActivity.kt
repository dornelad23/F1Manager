package com.example.f1manager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.TextView

class LoadingActivity : BaseActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var txtPorcentagem: TextView

    private var progresso = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loading)

        progressBar = findViewById(R.id.progressBar)
        txtPorcentagem = findViewById(R.id.txtPorcentagem)

        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {

            override fun run() {

                progresso += 2

                progressBar.progress = progresso
                txtPorcentagem.text = "$progresso%"

                if (progresso < 100) {

                    handler.postDelayed(this, 40)

                } else {

                    val intent = Intent(
                        this@LoadingActivity,
                        MenuActivity::class.java
                    )

                    startActivity(intent)

                    overridePendingTransition(
                        R.anim.fade_in,
                        R.anim.fade_out
                    )

                    finish()
                }
            }
        }

        handler.post(runnable)
    }
}