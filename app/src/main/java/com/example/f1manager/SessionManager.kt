package com.example.f1manager

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object SessionManager {

    fun abrirDestinoCorreto(context: Context) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            context.startActivity(Intent(context, LoginActivity::class.java))
            return
        }

        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .document(user.uid)
            .get()
            .addOnSuccessListener { doc ->

                if (doc.exists()) {
                    context.startActivity(Intent(context, GarageActivity::class.java))
                } else {
                    context.startActivity(Intent(context, CreateTeamActivity::class.java))
                }
            }
            .addOnFailureListener {
                context.startActivity(Intent(context, CreateTeamActivity::class.java))
            }
    }
}