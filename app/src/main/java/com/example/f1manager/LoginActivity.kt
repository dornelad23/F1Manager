package com.example.f1manager

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private var criandoConta = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editSenha = findViewById<EditText>(R.id.editSenha)

        val btnEntrar = findViewById<TextView>(R.id.btnEntrar)

        val btnGoogle = findViewById<LinearLayout>(R.id.btnGoogle)

        val textCriarConta =
            findViewById<TextView>(R.id.textCriarConta)

        val options = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(
                getString(R.string.default_web_client_id)
            )
            .requestEmail()
            .build()

        googleSignInClient =
            GoogleSignIn.getClient(this, options)

        btnEntrar.setOnClickListener {

            val email =
                editEmail.text.toString().trim()

            val senha =
                editSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {

                mostrarErro(
                    "Campo vazio",
                    "Preencha o email e senha."
                )

                return@setOnClickListener
            }

            if (criandoConta) {

                auth.createUserWithEmailAndPassword(
                    email,
                    senha
                )
                    .addOnSuccessListener {

                        abrirCriarEquipe()

                    }
                    .addOnFailureListener {

                        mostrarErro(
                            "Erro ao criar conta",
                            traduzirErro(it)
                        )
                    }

            } else {

                auth.signInWithEmailAndPassword(
                    email,
                    senha
                )
                    .addOnSuccessListener {

                        abrirCriarEquipe()

                    }
                    .addOnFailureListener {

                        mostrarErro(
                            "Erro ao entrar",
                            traduzirErro(it)
                        )
                    }
            }
        }

        textCriarConta.setOnClickListener {

            criandoConta = !criandoConta

            if (criandoConta) {

                btnEntrar.text = "CRIAR CONTA"

                textCriarConta.text =
                    "Já tem conta? Entrar"

            } else {

                btnEntrar.text = "ENTRAR"

                textCriarConta.text =
                    "Não tem conta? Criar conta"
            }
        }

        btnGoogle.setOnClickListener {

            startActivityForResult(
                googleSignInClient.signInIntent,
                1001
            )
        }
            }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == 1001) {

            try {

                val account =
                    GoogleSignIn
                        .getSignedInAccountFromIntent(data)
                        .getResult(ApiException::class.java)

                val credential =
                    GoogleAuthProvider.getCredential(
                        account.idToken,
                        null
                    )

                auth.signInWithCredential(
                    credential
                )
                    .addOnSuccessListener {

                        abrirCriarEquipe()

                    }
                    .addOnFailureListener {

                        mostrarErro(
                            "Erro Google",
                            traduzirErro(it)
                        )
                    }

            } catch (e: Exception) {

                mostrarErro(
                    "Erro Google",
                    e.message.toString()
                )
            }
        }
    }

    private fun abrirCriarEquipe() {

        startActivity(
            Intent(
                this,
                CreateTeamActivity::class.java
            )
        )

        overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out
        )

        finish()
    }

    private fun mostrarErro(
        titulo: String,
        mensagem: String
    ) {

        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensagem)
            .setPositiveButton(
                "OK",
                null
            )
            .show()
    }

    private fun traduzirErro(
        erro: Exception
    ): String {

        return when (erro) {

            is FirebaseAuthInvalidCredentialsException ->
                "Email ou senha inválidos."

            is FirebaseAuthInvalidUserException ->
                "Conta não encontrada."

            is FirebaseAuthUserCollisionException ->
                "Esse email já existe."

            is FirebaseAuthWeakPasswordException ->
                "Senha muito fraca."

            else ->
                erro.message
                    ?: "Erro inesperado."
        }
    }
}