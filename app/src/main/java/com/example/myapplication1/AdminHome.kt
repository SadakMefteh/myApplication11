package com.example.myapplication1
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class FirebaseAuthConnectivityTester {

    companion object {
        private const val TAG = "FirebaseAuthTest"
    }

    private val auth = FirebaseAuth.getInstance()

    /**
     * Teste si Firebase Auth est accessible.
     */
    fun testAuthConnection(
        onSuccess: () -> Unit,
        onFailure: (errorMessage: String) -> Unit
    ) {
        // On utilise un email invalide pour déclencher une erreur "attendue"
        val testEmail = "test_inexistant@example.com"
        val testPassword = "fakePassword123"

        auth.signInWithEmailAndPassword(testEmail, testPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Cas improbable (le compte n'existe pas)
                    onSuccess()
                    auth.signOut() // On se déconnecte
                } else {
                    when (task.exception) {
                        is FirebaseAuthInvalidUserException,
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Erreur attendue = Firebase est accessible !
                            Log.d(TAG, "Firebase Auth fonctionne (erreur normale)")
                            onSuccess()
                        }
                        else -> {
                            // Erreur réseau ou autre problème
                            onFailure(task.exception?.message ?: "Erreur inconnue")
                        }
                    }
                }
            }
    }
}