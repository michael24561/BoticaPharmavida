package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView

class PerfilActivity : AppCompatActivity() {
    private lateinit var nameTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        nameTextView = findViewById(R.id.username)

        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        val editarPerfil = findViewById<ImageView>(R.id.ic_profile)
        val misPedidos = findViewById<ImageView>(R.id.pedidos)

        editarPerfil.setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        misPedidos.setOnClickListener {
            val intent = Intent(this, HistorialPedidosActivity::class.java)
            startActivity(intent)
        }




        nameTextView.text = username ?: "Nombre no disponible"

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
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

        val btnCerrarSesion = findViewById<LinearLayout>(R.id.btn_cerrar_sesion)
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        val mapButton = findViewById<ImageView>(R.id.ic_maps)
        mapButton.setOnClickListener {
            openMapsActivity()
        }
    }

    private fun openMapsActivity() {
        val intent = Intent(this, MapsActivity::class.java)

        val coordinates = arrayListOf(
            LatLng(-8.094023264182493, -79.03693931820501),
            LatLng(-8.09637611172991, -79.02977049308365),
            LatLng(-8.095781285121767, -79.0113168949252),
            LatLng(-8.102154382244429, -79.01063024939796),
            LatLng(-8.100000, -79.020000)
        )

        intent.putParcelableArrayListExtra("coordinates", coordinates)
        startActivity(intent)
    }


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

    override fun onStart() {
        super.onStart()
        val menuIcon: ImageView = findViewById(R.id.menu_icon)
        menuIcon.setOnClickListener {
            startActivity(Intent(this, MenuLateralActivity::class.java))
        }

    }
}
