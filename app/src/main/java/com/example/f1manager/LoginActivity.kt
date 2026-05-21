package com.example.f1manager

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    private var criandoConta = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        callbackManager = CallbackManager.Factory.create()

        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editSenha = findViewById<EditText>(R.id.editSenha)
        val btnEntrar = findViewById<TextView>(R.id.btnEntrar)
        val btnGoogle = findViewById<LinearLayout>(R.id.btnGoogle)
        val btnFacebook = findViewById<LinearLayout>(R.id.btnFacebook)
        val textCriarConta = findViewById<TextView>(R.id.textCriarConta)

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, options)

        btnEntrar.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val senha = editSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                mostrarErro("Campo vazio", "Preencha o email e a senha para continuar.")
                return@setOnClickListener
            }

            if (senha.length < 6) {
                mostrarErro("Senha muito curta", "A senha precisa ter pelo menos 6 caracteres.")
                return@setOnClickListener
            }

            if (criandoConta) {
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnSuccessListener { abrirJogo() }
                    .addOnFailureListener {
                        mostrarErro("Erro ao criar conta", traduzirErro(it))
                    }
            } else {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnSuccessListener { abrirJogo() }
                    .addOnFailureListener {
                        mostrarErro("Erro ao entrar", traduzirErro(it))
                    }
            }
        }

        textCriarConta.setOnClickListener {
            criandoConta = !criandoConta

            if (criandoConta) {
                btnEntrar.text = "CRIAR CONTA"
                textCriarConta.text = "Já tem conta? Entrar"
            } else {
                btnEntrar.text = "ENTRAR"
                textCriarConta.text = "Não tem conta? Criar conta"
            }
        }

        btnGoogle.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, 1001)
        }

        btnFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                listOf("email", "public_profile")
            )
        }

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult) {
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)

                    auth.signInWithCredential(credential)
                        .addOnSuccessListener { abrirJogo() }
                        .addOnFailureListener {
                            mostrarErro("Erro no Facebook", traduzirErro(it))
                        }
                }

                override fun onCancel() {
                    mostrarErro("Login cancelado", "Você cancelou o login com Facebook.")
                }

                override fun onError(error: FacebookException) {
                    mostrarErro("Erro no Facebook", error.message ?: "Não foi possível entrar com Facebook.")
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnSuccessListener { abrirJogo() }
                    .addOnFailureListener {
                        mostrarErro("Erro no Google", traduzirErro(it))
                    }

            } catch (e: Exception) {
                mostrarErro("Erro no Google", e.message ?: "Não foi possível entrar com Google.")
            }
        }
    }

    private fun abrirJogo() {
        startActivity(Intent(this, MenuActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun mostrarErro(titulo: String, mensagem: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensagem)
            .setPositiveButton("Entendi", null)
            .show()
    }

    private fun traduzirErro(erro: Exception): String {
        return when (erro) {
            is FirebaseAuthInvalidCredentialsException ->
                "Email ou senha estão errados. Confere os dados e tenta de novo."

            is FirebaseAuthInvalidUserException ->
                "Essa conta não existe. Crie uma conta primeiro."

            is FirebaseAuthUserCollisionException ->
                "Esse email já está cadastrado. Tenta entrar em vez de criar outra conta."

            is FirebaseAuthWeakPasswordException ->
                "A senha está fraca. Use pelo menos 6 caracteres."

            else ->
                erro.message ?: "Aconteceu um erro inesperado. Tenta novamente."
        }
    }
}