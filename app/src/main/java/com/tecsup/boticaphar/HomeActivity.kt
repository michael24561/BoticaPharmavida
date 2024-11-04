package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Obtén la referencia al BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menuIcon: ImageView = findViewById(R.id.menu_icon)

        // Establece el listener para los ítems seleccionados
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Aquí puedes manejar el caso si deseas permanecer en la misma actividad
                    Toast.makeText(this, "Estás en la página principal", Toast.LENGTH_SHORT).show()
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

        // Manejo de iconos de menú y notificaciones
        findViewById<View>(R.id.menu_icon).setOnClickListener {
            // Redirigir a la actividad activity_perfil_completo_drawer
            val intent = Intent(this, MenuLateralActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.notification_icon).setOnClickListener {
            val intent = Intent(this, NotificacionActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Abriendo notificaciones", Toast.LENGTH_SHORT).show()
        }
    }
}
