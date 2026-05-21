package com.example.f1manager

import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GarageActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garage)

        val txtNomeEquipe = findViewById<TextView>(R.id.txtNomeEquipe)
        val txtDinheiro = findViewById<TextView>(R.id.txtDinheiro)
        val txtCarroNivel = findViewById<TextView>(R.id.txtCarroNivel)
        val txtAtributos = findViewById<TextView>(R.id.txtAtributos)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        if (uid != null) {
            db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { doc ->

                    val nome = doc.getString("nomeEquipe") ?: "Minha Equipe"
                    val dinheiro = doc.getLong("dinheiro") ?: 0
                    val nivel = doc.getLong("carroNivel") ?: 1
                    val motor = doc.getLong("motor") ?: 10
                    val velocidade = doc.getLong("velocidade") ?: 10
                    val controle = doc.getLong("controle") ?: 10
                    val pneu = doc.getLong("pneu") ?: 10

                    txtNomeEquipe.text = nome
                    txtDinheiro.text = "R$ $dinheiro"
                    txtCarroNivel.text = "CARRO NÍVEL $nivel"

                    txtAtributos.text =
                        "Motor: $motor\nVelocidade: $velocidade\nControle: $controle\nPneu: $pneu"
                }
        }
    }
}