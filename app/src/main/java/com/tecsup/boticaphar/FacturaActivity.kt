package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.tecsup.boticaphar.adapters.FacturaAdapter
import com.tecsup.boticaphar.models.Producto

class FacturaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var facturaAdapter: FacturaAdapter
    private lateinit var productosCarrito: List<Producto> // Lista de productos del carrito
    private lateinit var totalFacturaText: TextView // Total de la factura
    private lateinit var usuarioText: TextView // Nombre del usuario

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factura)

        recyclerView = findViewById(R.id.recycler_view_factura)
        totalFacturaText = findViewById(R.id.total_factura_text)
        usuarioText = findViewById(R.id.usuario_text)

        // Aquí se obtienen los productos del carrito
        productosCarrito = obtenerProductosDelCarrito()

        // Aquí calculas el total de la factura sumando los productos en el carrito
        val totalFactura = calcularTotalFactura(productosCarrito)
        totalFacturaText.text = "Total: S/ ${String.format("%.2f", totalFactura)}"

        // Aquí se obtienen los datos del usuario (puedes obtenerlos desde el SharedPreferences o desde la API)
        val usuario = obtenerUsuario()
        usuarioText.text = "Usuario: ${usuario.nombre}"

        facturaAdapter = FacturaAdapter(productosCarrito)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = facturaAdapter
    }

    private fun obtenerProductosDelCarrito(): List<Producto> {
        // Aquí obtendrás los productos reales del carrito.
        val context = this // Suponiendo que estás en un Activity o Context

        val sharedPreferences = context.getSharedPreferences("carritoPrefs", Context.MODE_PRIVATE)
        val carritoJson = sharedPreferences.getString("carrito", "[]") // Obtenemos el carrito en formato JSON

        // Convertir el JSON en una lista de productos
        return parseCarritoDesdeJson(carritoJson.toString())
    }

    private fun parseCarritoDesdeJson(carritoJson: String): List<Producto> {
        // Convertir el JSON en objetos Producto
        val gson = Gson()
        val productoListType = object : TypeToken<List<Producto>>() {}.type
        return gson.fromJson(carritoJson, productoListType)
    }

    private fun calcularTotalFactura(productos: List<Producto>): Double {
        var total = 0.0
        for (producto in productos) {
            total += producto.precioUnitario * producto.cantidad
        }
        return total
    }

    private fun obtenerUsuario(): Usuario {
        // Aquí puedes obtener los datos del usuario desde SharedPreferences o API.
        // A continuación te muestro cómo obtenerlo desde SharedPreferences.
        val sharedPreferences = getSharedPreferences("usuarioPrefs", Context.MODE_PRIVATE)
        val usuarioJson = sharedPreferences.getString("usuario", "{}")

        // Convertir el JSON del usuario en objeto Usuario
        val gson = Gson()
        return gson.fromJson(usuarioJson, Usuario::class.java)
    }
}
