package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
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

        val editarPerfil = findViewById<ImageView>(R.id.ic_profile)
        val misPedidos = findViewById<ImageView>(R.id.pedidos)

        // Configurar el click listener para el botón de editar perfil
        editarPerfil.setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        // Configurar el click listener para el botón de mis pedidos
        misPedidos.setOnClickListener {
            val intent = Intent(this, HistorialPedidosActivity::class.java)
            startActivity(intent)
        }

        // Muestra los detalles en los TextViews
        nameTextView.text = username ?: "Nombre no disponible"
        emailTextView.text = email ?: "Email no disponible"

        // Configurar BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Establecer el ítem del perfil como seleccionado
        bottomNavigationView.selectedItemId = R.id.nav_profile

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_cart -> {
                    val intent = Intent(this, CarritoActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> true // Mantener en la misma actividad
                else -> false
            }
        }

        // Manejo del botón de cerrar sesión
        val btnCerrarSesion = findViewById<LinearLayout>(R.id.btn_cerrar_sesion)
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        // Agregar el OnClickListener para abrir Google Maps al hacer clic en el ícono de mapa
        val mapButton = findViewById<ImageView>(R.id.ic_maps)
        mapButton.setOnClickListener {
            openGoogleMaps()
        }
    }

    // Función para abrir Google Maps con una ubicación específica
    private fun openGoogleMaps() {
        val location = Uri.parse("geo:0,0?q=-8.094023264182493,-79.03693931820501") // Coordenadas de FarmaVida
        val mapIntent = Intent(Intent.ACTION_VIEW, location)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }
    // Función para cerrar sesión
    private fun cerrarSesion() {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
