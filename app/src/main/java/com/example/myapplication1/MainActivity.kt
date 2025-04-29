package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Créez un layout simple pour MainActivity

        // Redirigez directement vers l'écran d'inscription
        startActivity(Intent(this, Login::class.java))
        finish() // Ferme MainActivity pour qu'elle ne reste pas dans la pile
    }
}