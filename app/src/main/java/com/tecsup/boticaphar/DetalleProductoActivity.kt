package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tecsup.boticaphar.adapters.ProductoAdapter
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.network.ApiService
import com.tecsup.boticaphar.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var searchBar: EditText
    private var productosSimilares: List<Producto> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto_detalle)

        val menuRetroceder = findViewById<ImageView>(R.id.menu_retroceder3)
        menuRetroceder.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        searchBar = findViewById(R.id.search_bar)

        searchBar.setOnClickListener {
            val intent = Intent(this, BusquedaActivity::class.java)
            startActivity(intent)
        }

        val producto = intent.getParcelableExtra<Producto>("producto")

        producto?.let {
            // Mostrar los detalles del producto
            findViewById<TextView>(R.id.nombre_producto).text = it.nombre
            findViewById<TextView>(R.id.precio_producto).text = "$${it.precio}"
            findViewById<TextView>(R.id.descripcion_producto).text = it.descripcion
            findViewById<TextView>(R.id.presentacion).text = "Presentación: ${it.presentacion}"

            // Cargar la imagen usando Picasso
            Picasso.get().load(it.imagen).into(findViewById<ImageView>(R.id.imagen_producto))

            // Inicializar RecyclerView
            recyclerView = findViewById(R.id.recyclerview_similares)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            // Obtener productos similares (usando la categoría del producto)
            obtenerProductosSimilares(it.categoria, it.id)  // Usamos it.categoria directamente
        }
    }

    private fun obtenerProductosSimilares(categoriaId: Int, productoId: Int) {
        val apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        // Hacer la llamada a la API para obtener todos los productos
        apiService.obtenerProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val productos = response.body() ?: emptyList()

                    // Filtrar productos que tienen la misma categoría, pero excluyendo el producto actual
                    productosSimilares = productos.filter { it.categoria == categoriaId && it.id != productoId }

                    // Configurar el adaptador del RecyclerView con los productos similares
                    productoAdapter = ProductoAdapter(productosSimilares)
                    recyclerView.adapter = productoAdapter
                } else {
                    Toast.makeText(this@DetalleProductoActivity, "Error al cargar productos similares", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Toast.makeText(this@DetalleProductoActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
