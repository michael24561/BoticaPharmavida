package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""

        recyclerView = findViewById(R.id.carrito_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        totalPriceText = findViewById(R.id.total_price_text)

        val productosEnCarrito = Carrito.obtenerProductos(this, username).toMutableList()

        carritoAdapter = CarritoAdapter(productosEnCarrito) {
            actualizarVistaCarrito()
        }
        recyclerView.adapter = carritoAdapter

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

        val checkoutButton = findViewById<Button>(R.id.checkout_button)
        checkoutButton.setOnClickListener {
            if (productosEnCarrito.isNotEmpty()) {
                realizarPedido(username, productosEnCarrito)
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }

        actualizarVistaCarrito()
    }

    private fun actualizarVistaCarrito() {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""
        val productosEnCarrito = Carrito.obtenerProductos(this, username)

        val total = calcularTotalCarrito(productosEnCarrito)

        totalPriceText.text = "Total: S/ ${String.format("%.2f", total)}"

        carritoAdapter.notifyDataSetChanged()

        if (productosEnCarrito.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calcularTotalCarrito(productosEnCarrito: List<Producto>): Double {
        var total = 0.0
        productosEnCarrito.forEach { producto ->
            total += producto.precio * producto.cantidad
        }
        return total
    }

    private fun realizarPedido(username: String, productosEnCarrito: List<Producto>) {
        val totalPedido = calcularTotalCarrito(productosEnCarrito)

        val fechaPedido = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val estadoPedido = "Pendiente"

        val pedido = Pedido(
            id = 0,
            fecha_pedido = fechaPedido,
            total_pedido = totalPedido,
            estado = estadoPedido,
            cantidad = productosEnCarrito.sumOf { it.cantidad },
            precio_compra = totalPedido,
            producto = productosEnCarrito.firstOrNull()?.id ?: 0,
            proveedor = 1
        )

        Log.d("CarritoActivity", "Realizando pedido con los siguientes datos: $pedido")
        RetrofitClient.instance.realizarPedido(pedido).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@CarritoActivity,
                        "Pedido realizado con éxito",
                        Toast.LENGTH_SHORT
                    ).show()
                    Carrito.vaciarCarrito(this@CarritoActivity, username)
                    startActivity(Intent(this@CarritoActivity, MetodosPagoActivity::class.java))
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
