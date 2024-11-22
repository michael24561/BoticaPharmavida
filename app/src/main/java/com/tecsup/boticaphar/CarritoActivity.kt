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

        // Listener para el BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // Nueva línea para finalizar la actividad actual
                    true
                }
                R.id.nav_cart -> true
                R.id.nav_profile -> {
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    finish() // Nueva línea para finalizar la actividad actual
                    true
                }
                else -> false
            }
        }

        // Botón para regresar al menú principal
        val menuRetroceder = findViewById<ImageView>(R.id.menu_retroceder)
        menuRetroceder.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Reinicia la actividad principal
            startActivity(intent)
            finish()
        }

        // Cargar productos del carrito (Nueva función)
        cargarProductosCarrito()
    }

    // Función para cargar los productos en el carrito
    private fun cargarProductosCarrito() {
        // Aquí deberías obtener los productos del carrito (puede ser de una base de datos local, servidor, o memoria)
        // Por ejemplo:
        val productosCarrito = listOf("Producto 1", "Producto 2", "Producto 3") // Simulación de datos
        for (producto in productosCarrito) {
            println("Producto en el carrito: $producto") // Puedes usar un RecyclerView para mostrarlos
        }
    }
}
