package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tecsup.boticaphar.adapters.CarritoAdapter
import com.tecsup.boticaphar.models.Carrito
import com.tecsup.boticaphar.models.Pedido
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.network.RetrofitClient
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarritoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carritoAdapter: CarritoAdapter
    private lateinit var totalPriceText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        // Obtener el nombre de usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""

        // Configurar el botón retroceder
        val retrocederButton = findViewById<ImageView>(R.id.menu_retroceder)
        retrocederButton.setOnClickListener {
            onBackPressed() // Retrocede a la actividad anterior
        }

        // Configurar RecyclerView para mostrar los productos del carrito
        recyclerView = findViewById(R.id.carrito_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar el texto para mostrar el total del carrito
        totalPriceText = findViewById(R.id.total_price_text)

        // Obtener los productos en el carrito
        val productosEnCarrito = Carrito.obtenerProductos(this, username).toMutableList()

        // Configurar el adaptador del RecyclerView
        carritoAdapter = CarritoAdapter(productosEnCarrito) {
            actualizarVistaCarrito()
        }
        recyclerView.adapter = carritoAdapter

        // Configurar el BottomNavigationView
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

        // Configurar el botón de checkout
        val checkoutButton = findViewById<Button>(R.id.checkout_button)
        checkoutButton.setOnClickListener {
            if (productosEnCarrito.isNotEmpty()) {
                realizarPedido(username, productosEnCarrito)
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }

        // Actualizar la vista al cargar la actividad
        actualizarVistaCarrito()
    }

    private fun actualizarVistaCarrito() {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""
        val productosEnCarrito = Carrito.obtenerProductos(this, username)

        if (productosEnCarrito.isEmpty()) {
            totalPriceText.text = "El carrito está vacío"
        } else {
            val total = calcularTotalCarrito(productosEnCarrito)
            totalPriceText.text = "Total: S/ ${String.format("%.2f", total)}"
        }

        carritoAdapter.notifyDataSetChanged()
    }

    private fun calcularTotalCarrito(productosEnCarrito: List<Producto>): Double {
        return productosEnCarrito.sumOf { it.precio * it.cantidad }
    }

    private fun realizarPedido(username: String, productosEnCarrito: List<Producto>) {
        val totalPedido = calcularTotalCarrito(productosEnCarrito)
        val fechaPedido = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val estadoPedido = "Pendiente"

        // Verificar que los productos en el carrito no están vacíos
        if (productosEnCarrito.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío, no se puede realizar el pedido", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear objeto Pedido
        val pedido = Pedido(
            id = 0,
            fecha_pedido = fechaPedido,
            total_pedido = totalPedido,
            estado = estadoPedido,
            cantidad = productosEnCarrito.sumOf { it.cantidad },
            precio_compra = totalPedido,
            productoId = productosEnCarrito.firstOrNull()?.id ?: 0,
            proveedorId = 1
        )

        Log.d("CarritoActivity", "Realizando pedido con los siguientes datos: $pedido")

        // Realizar pedido a través de Retrofit
        RetrofitClient.instance.realizarPedido(pedido).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("CarritoActivity", "Pedido realizado con éxito.")
                    Toast.makeText(
                        this@CarritoActivity,
                        "Pedido realizado con éxito",
                        Toast.LENGTH_SHORT
                    ).show()
                    Carrito.vaciarCarrito(this@CarritoActivity, username)

                    // Verificar si el intento de redirección es correcto
                    try {
                        val intent = Intent(this@CarritoActivity, MetodosPagoActivity::class.java)
                        Log.d("CarritoActivity", "Redirigiendo a MetodosPagoActivity.")
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("CarritoActivity", "Error al redirigir: ${e.message}")
                    }
                } else {
                    Log.e("CarritoActivity", "Error al realizar el pedido: ${response.message()}")
                    Toast.makeText(
                        this@CarritoActivity,
                        "Error al realizar el pedido: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("CarritoActivity", "Error de conexión: ${t.message}")
                Toast.makeText(
                    this@CarritoActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
