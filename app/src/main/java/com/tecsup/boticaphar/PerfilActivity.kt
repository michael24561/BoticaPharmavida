package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
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

        editarPerfil.setOnClickListener {
            val intent = Intent(this@PerfilActivity, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        misPedidos.setOnClickListener {
            val intent = Intent(this@PerfilActivity, HistorialPedidosActivity::class.java)
            startActivity(intent)
        }

        // Muestra los detalles en los TextViews
        nameTextView.text = username ?: "Nombre no disponible"
        emailTextView.text = email ?: "Email no disponible"

        // Configurar BottomNavigationView si es necesario
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
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
                R.id.nav_profile -> true
                else -> false
            }
        }

        // Manejo de iconos de menú y notificaciones
        findViewById<View>(R.id.menu_icon).setOnClickListener {
            val intent = Intent(this, MenuLateralActivity::class.java)
            startActivity(intent)
        }

        // Manejo del botón de cerrar sesión
        val btnCerrarSesion = findViewById<LinearLayout>(R.id.btn_cerrar_sesion)
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    // Función para cerrar sesión
    private fun cerrarSesion() {
        // Eliminar los datos del usuario de SharedPreferences
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Borra todos los datos almacenados
        editor.apply()

        // Redirigir a la pantalla de inicio de sesión
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Cerrar la actividad actual para evitar que el usuario regrese
    }
}
