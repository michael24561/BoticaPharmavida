package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Establece el tema antes de llamar a onCreate
        setTheme(R.style.AppTheme) // Aplica el tema definido en styles.xml

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn() {
        val sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("access_token", null)

        if (!accessToken.isNullOrEmpty()) {
            // Si ya hay un token, ir directamente al HomeActivity
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        } else {
            // Si no hay token, ir a la pantalla de instrucciones o login
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@MainActivity, Inicio1Activity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }
    }
}
