package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var signupLink: TextView
    private lateinit var forgotPasswordLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialisation Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialisation des vues
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.login)
        signupLink = findViewById(R.id.lienSignup)
        forgotPasswordLink = findViewById(R.id.lienForgetpass)

        // Gestion du clic sur le bouton de connexion
        loginButton.setOnClickListener {
            loginUser()
        }

        // Gestion du lien vers l'inscription
        signupLink.setOnClickListener {
            navigateToRegister()
        }

        // Gestion du lien "Forgot Password"
        forgotPasswordLink.setOnClickListener {
            resetPassword()
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validation des champs
        if (!validateEmail(email)) return
        if (!validatePassword(password)) return

        // Authentification avec Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Connexion réussie
                    Toast.makeText(this, "Connexion réussie!", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                } else {
                    // Gestion des erreurs
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "Aucun compte associé à cet email"
                        is FirebaseAuthInvalidCredentialsException -> "Email ou mot de passe incorrect"
                        else -> "Erreur de connexion: ${task.exception?.message}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun resetPassword() {
        val email = emailEditText.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer votre email", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Veuillez entrer un email valide", Toast.LENGTH_SHORT).show()
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Email de réinitialisation envoyé à $email",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Échec de l'envoi de l'email de réinitialisation: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun validateEmail(email: String): Boolean {
        return when {
            email.isEmpty() -> {
                showError("L'email est requis")
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showError("Veuillez entrer un email valide")
                false
            }
            else -> true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return when {
            password.isEmpty() -> {
                showError("Le mot de passe est requis")
                false
            }
            password.length < 6 -> {
                showError("Le mot de passe doit contenir au moins 6 caractères")
                false
            }
            else -> true
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, ClientHome::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        // Vérifier si l'utilisateur est déjà connecté
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToMainActivity()
        }
    }
}