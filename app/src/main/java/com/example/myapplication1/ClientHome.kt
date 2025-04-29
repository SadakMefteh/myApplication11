package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ClientHome : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)

        // Initialisation Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Récupération du bouton de déconnexion depuis votre layout
        val logoutButton: ImageView = findViewById(R.id.logoutButton)

        // Gestion du clic sur le bouton de déconnexion
        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        auth.signOut() // Déconnexion de Firebase

        // Redirection vers l'écran de login
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish() // Ferme l'activité actuelle

        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        // Vérification si l'utilisateur est toujours connecté
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Si l'utilisateur n'est pas connecté, rediriger vers Login
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}