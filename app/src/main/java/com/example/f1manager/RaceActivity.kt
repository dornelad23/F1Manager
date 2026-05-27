package com.example.f1manager

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.random.Random

class RaceActivity : BaseActivity() {

    private lateinit var txtVolta: TextView
    private lateinit var txtPosicao: TextView
    private lateinit var txtTempo: TextView
    private lateinit var txtEvento: TextView
    private lateinit var txtPneus: TextView
    private lateinit var txtCombustivel: TextView
    private lateinit var btnIniciar: TextView

    private var pneuEscolhido = "MÉDIO"

    private var volta = 1
    private val totalVoltas = 20
    private var pneus = 100
    private var combustivel = 100
    private var estrategia = "NORMAL"

    private val pilotos = mutableListOf(
        "VER", "HAM", "LEC", "NOR", "SAI",
        "PER", "RUS", "ALO", "PIA", "VOCÊ",
        "GAS", "OCO", "TSU", "ALB", "MAG",
        "HUL", "BOT", "STR", "ZHO", "SAR"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_race)

        txtVolta = findViewById(R.id.txtVolta)
        txtPosicao = findViewById(R.id.txtPosicao)
        txtTempo = findViewById(R.id.txtTempo)
        txtPneus = findViewById(R.id.txtPneus)
        txtCombustivel = findViewById(R.id.txtCombustivel)
        btnIniciar = findViewById(R.id.btnIniciarCorrida)
        txtClima = findViewById(R.id.txtClima)
        txtRitmo = findViewById(R.id.txtRitmo)
        listaRanking = findViewById(R.id.listaRanking)


        atualizarTela()

        btnIniciar.setOnClickListener {
            iniciarCorrida()
        }


    }

    private lateinit var txtClima: TextView
    private lateinit var txtRitmo: TextView
    private lateinit var listaRanking: LinearLayout

    private fun iniciarCorrida() {
        btnIniciar.text = "SIMULANDO..."
        btnIniciar.isEnabled = false

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                simularVolta()
                atualizarTela()

                volta++

                if (volta <= totalVoltas) {
                    Handler(Looper.getMainLooper()).postDelayed(this, 2300)
                } else {
                    finalizarCorrida()
                }
            }
        }, 800)
    }

    private fun simularVolta() {
        val posAtual = pilotos.indexOf("VOCÊ") + 1

        val gastoComb = when (estrategia) {
            "CONSERVADOR" -> Random.nextInt(2, 4)
            "AGRESSIVO" -> Random.nextInt(5, 8)
            else -> Random.nextInt(3, 6)
        }

        val gastoBasePneu = when (pneuEscolhido) {
            "MACIO" -> Random.nextInt(5, 8)
            "DURO" -> Random.nextInt(2, 4)
            else -> Random.nextInt(3, 6)
        }

        val gastoPneu = when (estrategia) {
            "CONSERVADOR" -> (gastoBasePneu - 1).coerceAtLeast(1)
            "AGRESSIVO" -> gastoBasePneu + 2
            else -> gastoBasePneu
        }

        pneus = (pneus - gastoPneu).coerceAtLeast(0)
        combustivel = (combustivel - gastoComb).coerceAtLeast(0)

        val chance = when (estrategia) {
            "CONSERVADOR" -> 45
            "AGRESSIVO" -> 72
            else -> 58
        }

        val penalidade = if (pneus < 30 || combustivel < 20) 25 else 0
        val sorte = Random.nextInt(100)

        if (sorte < chance - penalidade && posAtual > 1) {
            trocarPosicao(posAtual - 1)
            txtEvento.text = "Boa volta! Você ganhou posição."
        } else if (sorte > 82 || pneus < 12) {
            if (posAtual < 20) trocarPosicao(posAtual + 1)
            txtEvento.text = "Ritmo caiu. Você perdeu posição."
        } else {
            txtEvento.text = "Volta estável. Mantendo ritmo."
        }

        val tempoBase = 88.0
        val desgaste = (100 - pneus) * 0.025
        val bonusPneu = when (pneuEscolhido) {
            "MACIO" -> -0.7
            "DURO" -> 0.6
            else -> 0.0
        }

        val modo = when (estrategia) {
            "CONSERVADOR" -> 1.2
            "AGRESSIVO" -> -0.8
            else -> 0.0
        } + bonusPneu


    }

    private fun atualizarRankingVisual() {

        listaRanking.removeAllViews()

        pilotos.forEachIndexed { index, piloto ->

            val linha = LinearLayout(this)
            linha.orientation = LinearLayout.HORIZONTAL
            linha.setPadding(8, 8, 8, 8)

            if (piloto == "VOCÊ") {
                linha.setBackgroundColor(Color.parseColor("#1E8E3E"))
            } else {
                linha.setBackgroundColor(Color.parseColor("#101010"))
            }

            val posicao = TextView(this)
            posicao.text = "${index + 1}"
            posicao.setTextColor(Color.WHITE)
            posicao.textSize = 14f
            posicao.setTypeface(null, Typeface.BOLD)
            posicao.width = 40

            val nome = TextView(this)
            nome.text = piloto
            nome.setTextColor(Color.WHITE)
            nome.textSize = 13f
            nome.setTypeface(null, Typeface.BOLD)
            nome.layoutParams =
                LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f)

            val pneu = TextView(this)

            pneu.text = when (pneuEscolhido) {
                "MACIO" -> "S"
                "MÉDIO" -> "M"
                "DURO" -> "H"
                else -> "M"
            }

            pneu.setTextColor(
                when (pneuEscolhido) {
                    "MACIO" -> Color.RED
                    "MÉDIO" -> Color.YELLOW
                    "DURO" -> Color.WHITE
                    else -> Color.YELLOW
                }
            )

            pneu.textSize = 13f
            pneu.setTypeface(null, Typeface.BOLD)

            linha.addView(posicao)
            linha.addView(nome)
            linha.addView(pneu)

            listaRanking.addView(linha)
        }
    }

    private fun trocarPosicao(novaPosicao: Int) {
        val atual = pilotos.indexOf("VOCÊ")
        val novo = novaPosicao - 1

        if (atual in pilotos.indices && novo in pilotos.indices) {
            val outro = pilotos[novo]
            pilotos[novo] = "VOCÊ"
            pilotos[atual] = outro
        }
    }

    private fun atualizarTela() {
        val pos = pilotos.indexOf("VOCÊ") + 1

        txtVolta.text = "VOLTA $volta/$totalVoltas"
        txtPosicao.text = "P %02d".format(pos)
        txtPneus.text = "PNEUS $pneus%"
        txtCombustivel.text = "COMB. $combustivel%"

        atualizarRankingVisual()

    }

    private fun finalizarCorrida() {
        val pos = pilotos.indexOf("VOCÊ") + 1

        btnIniciar.text = "FINALIZADO"
        txtEvento.text = "Corrida finalizada! Você terminou em P$pos."

    }
}
