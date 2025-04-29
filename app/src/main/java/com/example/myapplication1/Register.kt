package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var signupButton: Button
    private lateinit var linkToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialisation des vues
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout)
        signupButton = findViewById(R.id.signupButton)
        linkToLogin = findViewById(R.id.linkToLogin)

        // Initialisation Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Gestion du clic sur le bouton d'inscription
        signupButton.setOnClickListener {
            registerUser()
        }

        // Lien vers le login
        linkToLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun registerUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        if (!validateEmail(email, emailInputLayout)) return
        if (!validatePassword(password, passwordInputLayout)) return
        if (!validateConfirmPassword(password, confirmPassword, confirmPasswordInputLayout)) return

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inscription réussie!", Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                } else {
                    Toast.makeText(
                        this,
                        "Échec de l'inscription: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish() // Ferme l'activité actuelle
    }

    private fun validateEmail(email: String, inputLayout: TextInputLayout): Boolean {
        return when {
            email.isEmpty() -> {
                inputLayout.error = "L'email est requis"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                inputLayout.error = "Veuillez entrer un email valide"
                false
            }
            else -> {
                inputLayout.error = null
                true
            }
        }
    }

    private fun validatePassword(password: String, inputLayout: TextInputLayout): Boolean {
        return when {
            password.isEmpty() -> {
                inputLayout.error = "Le mot de passe est requis"
                false
            }
            password.length < 6 -> {
                inputLayout.error = "Le mot de passe doit contenir au moins 6 caractères"
                false
            }
            else -> {
                inputLayout.error = null
                true
            }
        }
    }

    private fun validateConfirmPassword(
        password: String,
        confirmPassword: String,
        inputLayout: TextInputLayout
    ): Boolean {
        return when {
            confirmPassword.isEmpty() -> {
                inputLayout.error = "Veuillez confirmer votre mot de passe"
                false
            }
            password != confirmPassword -> {
                inputLayout.error = "Les mots de passe ne correspondent pas"
                false
            }
            else -> {
                inputLayout.error = null
                true
            }
        }
    }
}