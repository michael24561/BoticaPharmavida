package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

    class PerfilActivity : AppCompatActivity() {
        private lateinit var nameTextView: TextView
        private lateinit var emailTextView: TextView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_perfil)

            nameTextView = findViewById(R.id.username)
            emailTextView = findViewById(R.id.et_email)

            // Cargar datos de SharedPreferences
            val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)
            val email = sharedPreferences.getString("email", null)

            // Muestra los detalles en los TextViews
            nameTextView.text = username ?: "Nombre no disponible"
            emailTextView.text = email ?: "Email no disponible"

            // Configurar BottomNavigationView si es necesario...

    // Obtén la referencia al BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Establece el listener para los ítems seleccionados
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Navegar a la actividad Home
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_cart -> {
                    // Navegar a la actividad de carrito
                    val intent = Intent(this, CarritoActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Mantente en la actividad actual (Perfil)
                    true
                }
                else -> false
            }
        }
    }
}
