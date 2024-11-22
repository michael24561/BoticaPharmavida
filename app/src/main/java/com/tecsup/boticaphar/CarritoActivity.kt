package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CarritoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.nav_cart

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

        // Configura el evento de clic para menu_retroceder
        val menuRetroceder = findViewById<ImageView>(R.id.menu_retroceder)
        menuRetroceder.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
