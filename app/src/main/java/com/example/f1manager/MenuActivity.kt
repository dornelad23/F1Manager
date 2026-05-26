package com.example.f1manager

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        setContentView(R.layout.activity_menu)

        val btnJogar = findViewById<View>(R.id.btnJogar)
        val btnOpcoes = findViewById<View>(R.id.btnOpcoes)
        val btnSair = findViewById<View>(R.id.btnSair)

        // Se o botão garagem ainda existir no XML, ele fica invisível
        findViewById<View?>(R.id.btnGaragem)?.visibility = View.GONE

        btnJogar.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser

            val intent = if (user == null) {
                Intent(this, LoginActivity::class.java)
            } else {
                Intent(this, GarageActivity::class.java)
            }

            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        btnOpcoes.setOnClickListener {
            abrirOpcoes()
        }

        btnSair.setOnClickListener {
            finish()
        }
    }

    private fun abrirOpcoes() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_menu_opcoes)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val prefs = getSharedPreferences("config_jogo", MODE_PRIVATE)

        val abaBasica = dialog.findViewById<TextView>(R.id.abaBasica)
        val abaTela = dialog.findViewById<TextView>(R.id.abaTela)
        val abaConta = dialog.findViewById<TextView>(R.id.abaConta)

        val titulo = dialog.findViewById<TextView>(R.id.txtTituloAba)

        val conteudoBasica = dialog.findViewById<LinearLayout>(R.id.conteudoBasica)
        val conteudoTela = dialog.findViewById<LinearLayout>(R.id.conteudoTela)
        val conteudoConta = dialog.findViewById<LinearLayout>(R.id.conteudoConta)

        fun abrirAba(nome: String) {
            titulo.text = nome

            conteudoBasica.visibility = if (nome == "BÁSICA") View.VISIBLE else View.GONE
            conteudoTela.visibility = if (nome == "TELA") View.VISIBLE else View.GONE
            conteudoConta.visibility = if (nome == "CONTA") View.VISIBLE else View.GONE

            abaBasica.setTextColor(if (nome == "BÁSICA") 0xFFFFD84A.toInt() else 0xFFBBBBBB.toInt())
            abaTela.setTextColor(if (nome == "TELA") 0xFFFFD84A.toInt() else 0xFFBBBBBB.toInt())
            abaConta.setTextColor(if (nome == "CONTA") 0xFFFFD84A.toInt() else 0xFFBBBBBB.toInt())
        }

        abaBasica.setOnClickListener { abrirAba("BÁSICA") }
        abaTela.setOnClickListener { abrirAba("TELA") }
        abaConta.setOnClickListener { abrirAba("CONTA") }

        val switchMusica = dialog.findViewById<Switch>(R.id.switchMusica)
        val switchEfeitos = dialog.findViewById<Switch>(R.id.switchEfeitos)
        val switchVibracao = dialog.findViewById<Switch>(R.id.switchVibracao)
        val switchAnimacoes = dialog.findViewById<Switch>(R.id.switchAnimacoes)
        val switchPerformance = dialog.findViewById<Switch>(R.id.switchPerformance)
        val switchHUD = dialog.findViewById<Switch>(R.id.switchHUD)

        switchMusica.isChecked = prefs.getBoolean("musica", true)
        switchEfeitos.isChecked = prefs.getBoolean("efeitos", true)
        switchVibracao.isChecked = prefs.getBoolean("vibracao", true)
        switchAnimacoes.isChecked = prefs.getBoolean("animacoes", true)
        switchPerformance.isChecked = prefs.getBoolean("performance", false)
        switchHUD.isChecked = prefs.getBoolean("hud_minimalista", false)

        switchMusica.setOnCheckedChangeListener { _, v -> prefs.edit().putBoolean("musica", v).apply() }
        switchEfeitos.setOnCheckedChangeListener { _, v -> prefs.edit().putBoolean("efeitos", v).apply() }
        switchVibracao.setOnCheckedChangeListener { _, v -> prefs.edit().putBoolean("vibracao", v).apply() }
        switchAnimacoes.setOnCheckedChangeListener { _, v -> prefs.edit().putBoolean("animacoes", v).apply() }
        switchPerformance.setOnCheckedChangeListener { _, v -> prefs.edit().putBoolean("performance", v).apply() }
        switchHUD.setOnCheckedChangeListener { _, v -> prefs.edit().putBoolean("hud_minimalista", v).apply() }

        dialog.findViewById<TextView>(R.id.btnFecharOpcoes).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<TextView>(R.id.btnSairConta).setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        dialog.findViewById<TextView>(R.id.btnExcluirConta).setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.delete()
            finish()
        }

        abrirAba("BÁSICA")
        dialog.show()
    }
    }