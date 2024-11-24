package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tecsup.boticaphar.adapters.CarritoAdapter
import com.tecsup.boticaphar.adapters.ProductoAdapter
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.utils.Carrito

class CarritoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carritoAdapter: CarritoAdapter // Nuevo adaptador personalizado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.carrito_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener productos en el carrito
        val productosEnCarrito = Carrito.obtenerProductos().toMutableList()

        // Configurar el nuevo adaptador personalizado para carrito
        carritoAdapter = CarritoAdapter(productosEnCarrito) {
            actualizarVistaCarrito() // Actualizar la vista si el carrito cambia
        }
        recyclerView.adapter = carritoAdapter

        // Configurar el botón "Finalizar Compra"
        val checkoutButton = findViewById<Button>(R.id.checkout_button)
        checkoutButton.setOnClickListener {
            if (productosEnCarrito.isNotEmpty()) {
                Toast.makeText(this, "Compra finalizada con éxito", Toast.LENGTH_SHORT).show()
                Carrito.vaciarCarrito()
                actualizarVistaCarrito()
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_cart
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_cart -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Configurar botón para retroceder
        val menuRetroceder = findViewById<ImageView>(R.id.menu_retroceder)
        menuRetroceder.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun actualizarVistaCarrito() {
        carritoAdapter.notifyDataSetChanged()
        if (Carrito.obtenerProductos().isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
        }
    }
}
