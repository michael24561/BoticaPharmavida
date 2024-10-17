package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Obtén la referencia al BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Establece el listener para los ítems seleccionados
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Aquí puedes manejar el caso si deseas permanecer en la misma actividad
                    true
                }
                R.id.nav_cart -> {
                    // Navegar a la actividad de carrito
                    val intent = Intent(this, CarritoActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    // Navegar a la actividad de perfil
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        findViewById<View>(R.id.menu_icon).setOnClickListener {
            val intent = Intent(this, MenuLateralActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.notification_icon).setOnClickListener {
            val intent = Intent(this, NotificacionActivity::class.java)
            startActivity(intent)
        }
    }
}
