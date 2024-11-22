package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.boticaphar.adapters.ProductoAdapter
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.network.ApiService
import com.tecsup.boticaphar.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductosCategoriaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private var productos: List<Producto> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos_categoria)

        val menuRetroceder = findViewById<ImageView>(R.id.menu_retroceder2)
        menuRetroceder.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Obtener el ID y nombre de la categoría del Intent
        val categoriaId = intent.getIntExtra("categoriaId", -1)
        val categoriaNombre = intent.getStringExtra("categoriaNombre") ?: ""

        // Establecer el nombre de la categoría en el TextView en lugar del título
        val categoriaNombreTextView = findViewById<TextView>(R.id.categoria_nombre)
        categoriaNombreTextView.text = categoriaNombre

        if (categoriaId != -1) {
            // Inicializar RecyclerView
            recyclerView = findViewById(R.id.productos_recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(this)

            // Llamar a la función para obtener productos por categoría
            obtenerProductosPorCategoria(categoriaId)
        } else {
            Toast.makeText(this, "ID de categoría no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerProductosPorCategoria(categoriaId: Int) {
        val apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        // Llamada a la API para obtener productos por categoría
        apiService.obtenerProductosPorCategoria(categoriaId).enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val productosObtenidos = response.body() ?: emptyList()

                    // Si hay productos, los asignamos al RecyclerView
                    if (productosObtenidos.isNotEmpty()) {
                        productos = productosObtenidos
                        productoAdapter = ProductoAdapter(productos)
                        recyclerView.adapter = productoAdapter
                    } else {
                        Toast.makeText(this@ProductosCategoriaActivity, "No hay productos en esta categoría", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ProductosCategoriaActivity, "Error al cargar los productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Toast.makeText(this@ProductosCategoriaActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
