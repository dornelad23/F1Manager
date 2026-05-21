package com.example.f1manager

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateTeamActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_team)

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val uid = auth.currentUser?.uid

        val editNomeEquipe = findViewById<EditText>(R.id.editNomeEquipe)
        val btnCriarEquipe = findViewById<TextView>(R.id.btnCriarEquipe)

        btnCriarEquipe.setOnClickListener {

            Toast.makeText(this, "Botão clicado", Toast.LENGTH_SHORT).show()

            val nomeEquipe = editNomeEquipe.text.toString().trim()

            if (nomeEquipe.isEmpty()) {
                Toast.makeText(this, "Digite o nome da equipe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (uid == null) {
                Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val equipe = hashMapOf(
                "nomeEquipe" to nomeEquipe,
                "dinheiro" to 50000,
                "carroNivel" to 1,
                "motor" to 10,
                "velocidade" to 10,
                "controle" to 10,
                "pneu" to 10,
                "temporada" to 2026
            )

            db.collection("usuarios")
                .document(uid)
                .set(equipe)
                .addOnSuccessListener {
                    Toast.makeText(this, "Equipe criada com sucesso!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, GarageActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao criar equipe: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}