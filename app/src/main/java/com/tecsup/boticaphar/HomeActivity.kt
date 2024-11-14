package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tecsup.boticaphar.adapters.CategoriaAdapter
import com.tecsup.boticaphar.models.Categoria
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.network.ApiService
import com.tecsup.boticaphar.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriaAdapter: CategoriaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.category_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Iniciar la carga de categorías y productos
        obtenerCategorias()
    }

    private fun obtenerCategorias() {
        val apiService = RetrofitClient.getInstance().create(ApiService::class.java)
        apiService.obtenerCategorias().enqueue(object : Callback<List<Categoria>> {
            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    val categorias = response.body() ?: emptyList()
                    if (categorias.isNotEmpty()) {
                        // Después de obtener las categorías, obtenemos los productos
                        obtenerProductos(categorias)
                    } else {
                        Toast.makeText(this@HomeActivity, "No se encontraron categorías", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Error al obtener categorías", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun obtenerProductos(categorias: List<Categoria>) {
        val apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        // Llamada para obtener todos los productos, sin filtros
        apiService.obtenerProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val productos = response.body() ?: emptyList()

                    // Asignar productos a su categoría correspondiente
                    categorias.forEach { categoria ->
                        categoria.productos = productos.filter { it.categoria == categoria.id }
                    }

                    // Configurar el adaptador de categorías
                    categoriaAdapter = CategoriaAdapter(categorias)
                    recyclerView.adapter = categoriaAdapter
                } else {
                    Toast.makeText(this@HomeActivity, "Error al obtener productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error al obtener productos: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        // Configurar BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menuIcon: ImageView = findViewById(R.id.menu_icon)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Estás en la página principal", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this, CarritoActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Manejo de iconos de menú y notificaciones
        menuIcon.setOnClickListener {
            startActivity(Intent(this, MenuLateralActivity::class.java))
        }

        findViewById<View>(R.id.notification_icon).setOnClickListener {
            startActivity(Intent(this, NotificacionActivity::class.java))
            Toast.makeText(this, "Abriendo notificaciones", Toast.LENGTH_SHORT).show()
        }
    }
}
