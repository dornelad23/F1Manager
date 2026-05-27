package com.example.f1manager

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.app.Dialog
import android.widget.Switch
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout

class GarageActivity : BaseActivity() {

    private lateinit var txtNomeEquipe: TextView
    private lateinit var txtDinheiro: TextView

    private lateinit var txtPrecoMotor: TextView
    private lateinit var txtPrecoAero: TextView
    private lateinit var txtPrecoPneus: TextView
    private lateinit var txtPrecoControle: TextView

    private lateinit var btnUpgradeMotor: TextView
    private lateinit var btnUpgradeAero: TextView
    private lateinit var btnUpgradePneus: TextView
    private lateinit var btnUpgradeControle: TextView

    private val db = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    private var dinheiro = 0L

    private var nivelMotor = 1L
    private var nivelAero = 1L
    private var nivelPneus = 1L
    private var nivelControle = 1L

    private var precoMotor = 5000L
    private var precoAero = 4500L
    private var precoPneus = 3500L
    private var precoControle = 4000L

    private lateinit var txtNivelMotor: TextView
    private lateinit var txtNivelAero: TextView
    private lateinit var txtNivelPneus: TextView
    private lateinit var txtNivelControle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garage)

        val btnConfig = findViewById<android.widget.ImageButton>(R.id.btnConfig)

        btnConfig.setOnClickListener {
            abrirConfiguracoes()
        }
        val cardRank = findViewById<LinearLayout>(R.id.cardRank)

        cardRank.setOnClickListener {
            abrirRanking()
        }
        val cardEquipe = findViewById<LinearLayout>(R.id.cardEquipe)

        cardEquipe.setOnClickListener {
            abrirPerfilEquipe()
        }
        val btnVoltar = findViewById<LinearLayout>(R.id.btnVoltar)

        btnVoltar.setOnClickListener {
            finish()
        }
        val btnCorrida = findViewById<LinearLayout>(R.id.cardCorrida)

        btnCorrida.setOnClickListener {
            startActivity(
                android.content.Intent(
                    this,
                    RaceActivity::class.java
                )
            )
        }
        val cardCorrida = findViewById<LinearLayout>(R.id.cardCorrida)

        cardCorrida.setOnClickListener {

            startActivity(
                android.content.Intent(
                    this,
                    RaceActivity::class.java
                )
            )

            overridePendingTransition(
                R.anim.fade_in,
                R.anim.fade_out
            )
        }

        txtNivelMotor = findViewById(R.id.txtNivelMotor)
        txtNivelAero = findViewById(R.id.txtNivelAero)
        txtNivelPneus = findViewById(R.id.txtNivelPneus)
        txtNivelControle = findViewById(R.id.txtNivelControle)

        txtNomeEquipe = findViewById(R.id.txtNomeEquipe)
        txtDinheiro = findViewById(R.id.txtDinheiro)

        txtPrecoMotor = findViewById(R.id.txtPrecoMotor)
        txtPrecoAero = findViewById(R.id.txtPrecoAero)
        txtPrecoPneus = findViewById(R.id.txtPrecoPneus)
        txtPrecoControle = findViewById(R.id.txtPrecoControle)

        btnUpgradeMotor = findViewById(R.id.btnUpgradeMotor)
        btnUpgradeAero = findViewById(R.id.btnUpgradeAero)
        btnUpgradePneus = findViewById(R.id.btnUpgradePneus)
        btnUpgradeControle = findViewById(R.id.btnUpgradeControle)

        carregarDados()

        btnUpgradeMotor.setOnClickListener {
            comprarUpgrade("motor")
        }

        btnUpgradeAero.setOnClickListener {
            comprarUpgrade("aero")
        }

        btnUpgradePneus.setOnClickListener {
            comprarUpgrade("pneus")
        }

        btnUpgradeControle.setOnClickListener {
            comprarUpgrade("controle")
        }
    }

    private fun carregarDados() {
        if (uid == null) return

        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { doc ->
                txtNomeEquipe.text = doc.getString("nomeEquipe") ?: "Minha Equipe"

                dinheiro = doc.getLong("dinheiro") ?: 50000L

                nivelMotor = doc.getLong("nivelMotor") ?: 1L
                nivelAero = doc.getLong("nivelAero") ?: 1L
                nivelPneus = doc.getLong("nivelPneus") ?: 1L
                nivelControle = doc.getLong("nivelControle") ?: 1L

                atualizarTela()
            }
    }

    private fun abrirConfiguracoes() {

        val dialog = Dialog(this)

        dialog.setContentView(R.layout.dialog_config)

        val switchMusica = dialog.findViewById<Switch>(R.id.switchMusica)
        val switchEfeitos = dialog.findViewById<Switch>(R.id.switchEfeitos)
        val switchFPS = dialog.findViewById<Switch>(R.id.switchFPS)

        val btnResetar = dialog.findViewById<TextView>(R.id.btnResetar)
        val btnFechar = dialog.findViewById<TextView>(R.id.btnFecharConfig)

        btnFechar.setOnClickListener {
            dialog.dismiss()
        }

        btnResetar.setOnClickListener {

            if (uid == null) return@setOnClickListener

            val reset = hashMapOf<String, Any>(
                "dinheiro" to 50000,
                "nivelMotor" to 1,
                "nivelAero" to 1,
                "nivelPneus" to 1,
                "nivelControle" to 1
            )

            db.collection("usuarios")
                .document(uid)
                .update(reset)

            carregarDados()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun abrirRanking() {

        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(R.layout.dialog_rank)

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val listaRank = dialog.findViewById<LinearLayout>(R.id.listaRank)
        val btnFechar = dialog.findViewById<TextView>(R.id.btnFecharRank)

        btnFechar.setOnClickListener {
            dialog.dismiss()
        }

        db.collection("usuarios").get()
            .addOnSuccessListener { docs ->

                val equipes = docs.map { doc ->

                    val nome = doc.getString("nomeEquipe") ?: "Equipe"
                    val dinheiro = doc.getLong("dinheiro") ?: 0

                    val motor = doc.getLong("nivelMotor") ?: 1
                    val aero = doc.getLong("nivelAero") ?: 1
                    val pneus = doc.getLong("nivelPneus") ?: 1
                    val controle = doc.getLong("nivelControle") ?: 1

                    val pontos =
                        (motor + aero + pneus + controle) * 1000 +
                                dinheiro / 1000

                    EquipeRank(
                        nome,
                        dinheiro,
                        motor,
                        aero,
                        pneus,
                        controle,
                        pontos
                    )

                }.sortedByDescending { it.pontos }

                listaRank.removeAllViews()

                equipes.forEachIndexed { index, equipe ->

                    val card = LinearLayout(this)
                    card.orientation = LinearLayout.VERTICAL
                    card.setPadding(22, 18, 22, 18)

                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 0, 0, 14)
                    card.layoutParams = params
                    card.setBackgroundResource(R.drawable.bg_topbar)

                    val medalha = when (index) {
                        0 -> "🥇"
                        1 -> "🥈"
                        2 -> "🥉"
                        else -> "#${index + 1}"
                    }

                    val titulo = TextView(this)
                    titulo.text = "$medalha  ${equipe.nome}"
                    titulo.setTextColor(android.graphics.Color.WHITE)
                    titulo.textSize = if (index <= 2) 20f else 16f
                    titulo.typeface = android.graphics.Typeface.MONOSPACE
                    titulo.setTypeface(null, android.graphics.Typeface.BOLD)

                    val pontos = TextView(this)
                    pontos.text = "PONTOS: ${equipe.pontos}"
                    pontos.setTextColor(android.graphics.Color.parseColor("#39E246"))
                    pontos.textSize = 14f
                    pontos.typeface = android.graphics.Typeface.MONOSPACE
                    pontos.setTypeface(null, android.graphics.Typeface.BOLD)
                    pontos.setPadding(0, 8, 0, 4)

                    val info = TextView(this)
                    info.text =
                        "DINHEIRO: R$ ${formatar(equipe.dinheiro)}\n" +
                                "MOTOR ${equipe.motor}   AERO ${equipe.aero}   PNEUS ${equipe.pneus}   CONTROLE ${equipe.controle}"

                    info.setTextColor(android.graphics.Color.rgb(210, 210, 210))
                    info.textSize = 11f
                    info.typeface = android.graphics.Typeface.MONOSPACE

                    card.addView(titulo)
                    card.addView(pontos)
                    card.addView(info)

                    listaRank.addView(card)


                }
            }

        dialog.show()
    }
    private fun abrirPerfilEquipe() {

        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(R.layout.dialog_perfil_equipe)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val txtNome =
            dialog.findViewById<TextView>(R.id.txtPerfilNome)

        val txtEmail =
            dialog.findViewById<TextView>(R.id.txtPerfilEmail)

        val txtLogin =
            dialog.findViewById<TextView>(R.id.txtPerfilLogin)

        val btnLogout =
            dialog.findViewById<TextView>(R.id.btnLogout)

        val btnExcluir =
            dialog.findViewById<TextView>(R.id.btnExcluirConta)

        val btnFechar =
            dialog.findViewById<TextView>(R.id.btnFecharPerfil)

        val user = FirebaseAuth.getInstance().currentUser

        txtNome.text = txtNomeEquipe.text

        txtEmail.text =
            user?.email ?: "SEM EMAIL"

        txtLogin.text =
            "LOGIN: GOOGLE"

        btnFechar.setOnClickListener {
            dialog.dismiss()
        }

        btnLogout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            finish()
        }

        btnExcluir.setOnClickListener {

            user?.delete()

            finish()
        }

        dialog.show()
    }


    data class EquipeRank(
        val nome: String,
        val dinheiro: Long,
        val motor: Long,
        val aero: Long,
        val pneus: Long,
        val controle: Long,
        val pontos: Long
    )

    private fun comprarUpgrade(tipo: String) {
        if (uid == null) return

        val preco = when (tipo) {
            "motor" -> precoMotor
            "aero" -> precoAero
            "pneus" -> precoPneus
            "controle" -> precoControle
            else -> 0L
        }

        val nivelAtual = when (tipo) {
            "motor" -> nivelMotor
            "aero" -> nivelAero
            "pneus" -> nivelPneus
            "controle" -> nivelControle
            else -> 1L
        }

        if (nivelAtual >= 5) {
            return
        }

        if (dinheiro < preco) {
            Toast.makeText(this, "Dinheiro insuficiente", Toast.LENGTH_SHORT).show()
            return
        }

        dinheiro -= preco

        when (tipo) {
            "motor" -> nivelMotor++
            "aero" -> nivelAero++
            "pneus" -> nivelPneus++
            "controle" -> nivelControle++
        }

        val dados = hashMapOf<String, Any>(
            "dinheiro" to dinheiro,
            "nivelMotor" to nivelMotor,
            "nivelAero" to nivelAero,
            "nivelPneus" to nivelPneus,
            "nivelControle" to nivelControle,
            "motor" to nivelMotor * 10,
            "velocidade" to nivelAero * 10,
            "pneu" to nivelPneus * 10,
            "controle" to nivelControle * 10
        )

        db.collection("usuarios").document(uid)
            .update(dados)
            .addOnSuccessListener {
                atualizarTela()
            }
    }

    private fun atualizarBarra(nivel: Long, barras: List<View>) {
        barras.forEachIndexed { index, view ->
            view.setBackgroundColor(
                if (index < nivel) {
                    android.graphics.Color.parseColor("#35C846")
                } else {
                    android.graphics.Color.parseColor("#252525")
                }
            )
        }
    }

    private fun atualizarTela() {
        precoMotor = 5000L * nivelMotor
        precoAero = 4500L * nivelAero
        precoPneus = 3500L * nivelPneus
        precoControle = 4000L * nivelControle

        txtDinheiro.text = "R$ ${formatar(dinheiro)}"

        txtNivelMotor.text = "NÍVEL $nivelMotor"
        txtNivelAero.text = "NÍVEL $nivelAero"
        txtNivelPneus.text = "NÍVEL $nivelPneus"
        txtNivelControle.text = "NÍVEL $nivelControle"

        txtPrecoMotor.text = if (nivelMotor >= 5) "NÍVEL MAX" else "R$ ${formatar(precoMotor)}"
        txtPrecoAero.text = if (nivelAero >= 5) "NÍVEL MAX" else "R$ ${formatar(precoAero)}"
        txtPrecoPneus.text = if (nivelPneus >= 5) "NÍVEL MAX" else "R$ ${formatar(precoPneus)}"
        txtPrecoControle.text = if (nivelControle >= 5) "NÍVEL MAX" else "R$ ${formatar(precoControle)}"

        atualizarBarra(nivelMotor, listOf(
            findViewById(R.id.barMotor1),
            findViewById(R.id.barMotor2),
            findViewById(R.id.barMotor3),
            findViewById(R.id.barMotor4),
            findViewById(R.id.barMotor5)
        ))

        atualizarBarra(nivelAero, listOf(
            findViewById(R.id.barAero1),
            findViewById(R.id.barAero2),
            findViewById(R.id.barAero3),
            findViewById(R.id.barAero4),
            findViewById(R.id.barAero5)
        ))

        atualizarBarra(nivelPneus, listOf(
            findViewById(R.id.barPneus1),
            findViewById(R.id.barPneus2),
            findViewById(R.id.barPneus3),
            findViewById(R.id.barPneus4),
            findViewById(R.id.barPneus5)
        ))

        atualizarBarra(nivelControle, listOf(
            findViewById(R.id.barControle1),
            findViewById(R.id.barControle2),
            findViewById(R.id.barControle3),
            findViewById(R.id.barControle4),
            findViewById(R.id.barControle5)
        ))
    }

    }

    private fun formatar(valor: Long): String {
        return "%,d".format(valor).replace(",", ".")
    }
