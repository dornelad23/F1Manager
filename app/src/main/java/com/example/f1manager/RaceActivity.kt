package com.example.f1manager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import kotlin.random.Random

class RaceActivity : BaseActivity() {

    private lateinit var txtVolta: TextView
    private lateinit var txtPosicao: TextView
    private lateinit var txtTempo: TextView
    private lateinit var txtRanking: TextView
    private lateinit var txtEvento: TextView
    private lateinit var txtStatus: TextView
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
        txtRanking = findViewById(R.id.txtRanking)
        txtEvento = findViewById(R.id.txtEvento)
        txtStatus = findViewById(R.id.txtStatus)
        txtPneus = findViewById(R.id.txtPneus)
        txtCombustivel = findViewById(R.id.txtCombustivel)
        btnIniciar = findViewById(R.id.btnIniciarCorrida)

        findViewById<TextView>(R.id.btnConservador).setOnClickListener {
            estrategia = "CONSERVADOR"
            txtEvento.text = "Estratégia alterada: conservador."
        }

        findViewById<TextView>(R.id.btnNormal).setOnClickListener {
            estrategia = "NORMAL"
            txtEvento.text = "Estratégia alterada: normal."
        }

        findViewById<TextView>(R.id.btnAgressivo).setOnClickListener {
            estrategia = "AGRESSIVO"
            txtEvento.text = "Estratégia alterada: agressivo."
        }

        atualizarTela()

        btnIniciar.setOnClickListener {
            iniciarCorrida()
        }

        findViewById<TextView>(R.id.btnPneuMacio).setOnClickListener {
            pneuEscolhido = "MACIO"
            txtEvento.text = "Pneu escolhido: macio. Mais rápido, desgasta mais."
        }

        findViewById<TextView>(R.id.btnPneuMedio).setOnClickListener {
            pneuEscolhido = "MÉDIO"
            txtEvento.text = "Pneu escolhido: médio. Equilibrado."
        }

        findViewById<TextView>(R.id.btnPneuDuro).setOnClickListener {
            pneuEscolhido = "DURO"
            txtEvento.text = "Pneu escolhido: duro. Mais lento, dura mais."
        }
    }

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

        val tempo = tempoBase + desgaste + modo + Random.nextDouble(-0.4, 0.8)
        txtTempo.text = formatarTempo(tempo)

        txtStatus.text =
            "MODO: $estrategia\n\n" +
                "PNEUS: $pneus%\n" +
                "COMBUSTÍVEL: $combustivel%\n" +
                "RITMO: ${if (estrategia == "AGRESSIVO") "ALTO" else if (estrategia == "CONSERVADOR") "BAIXO" else "NORMAL"}\n\n" +
                "Dica da equipe:\n" +
                if (pneus < 25) "Pneus estão acabando, cuidado nas próximas voltas."
                else "Carro está respondendo bem. Continue nesse ritmo."
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

        val rank = StringBuilder()

        pilotos.forEachIndexed { index, nome ->
            val lugar = index + 1
            val marcador = if (nome == "VOCÊ") "▶" else "|"
            val tempo = if (lugar == 1) "-" else "+${"%.3f".format(lugar * 1.142)}"

            rank.append("%2d $marcador %-5s %7s\n".format(lugar, nome, tempo))
        }

        txtRanking.text = rank.toString()
    }

    private fun finalizarCorrida() {
        val pos = pilotos.indexOf("VOCÊ") + 1

        btnIniciar.text = "FINALIZADO"
        txtEvento.text = "Corrida finalizada! Você terminou em P$pos."

        txtStatus.text = txtStatus.text.toString() + "\n\nRESULTADO FINAL: P$pos"    }

    private fun formatarTempo(segundos: Double): String {
        val min = (segundos / 60).toInt()
        val sec = segundos % 60
        return "%d:%06.3f".format(min, sec)
    }
}
