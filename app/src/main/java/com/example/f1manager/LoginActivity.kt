package com.example.f1manager

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
                    android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        setContentView(R.layout.activity_login)

        val btnGoogle = findViewById<TextView>(R.id.btnGoogle)
        val btnFacebook = findViewById<TextView>(R.id.btnFacebook)

        btnGoogle.setOnClickListener {

            Toast.makeText(
                this,
                "Login Google em breve",
                Toast.LENGTH_SHORT
            ).show()

        }

        btnFacebook.setOnClickListener {

            Toast.makeText(
                this,
                "Login Facebook em breve",
                Toast.LENGTH_SHORT
            ).show()

        }

        val btnEmail = findViewById<TextView>(R.id.btnEmail)

        btnEmail.setOnClickListener {

            val intent = Intent(this, EmailLoginActivity::class.java)
            startActivity(intent)

        }

    }
}