package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.tecsup.boticaphar.models.Categoria
import com.tecsup.boticaphar.network.ApiService
import com.tecsup.boticaphar.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuLateralActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_completo_drawer)

        drawerLayout = findViewById(R.id.drawer_menu_perfil_completo)
        navigationView = findViewById(R.id.nav_menu_completo_view)

        // Llamada a la API para obtener las categorías
        obtenerCategorias()

        // Configuración del menú lateral
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_categoria -> {
                    // Al presionar "Categoría", solo cerramos el menú sin hacer nada especial
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_contactanos -> {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://www.youtube.com/watch?v=uWfbR_juSdY")
                    }
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_sobre_nosotros -> {
                    val intent = Intent(this, SobrenosotrosActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }

    // Función para obtener las categorías desde la API
    private fun obtenerCategorias() {
        val apiService = RetrofitClient.getInstance().create(ApiService::class.java)
        apiService.obtenerCategorias().enqueue(object : Callback<List<Categoria>> {
            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    val categorias = response.body() ?: emptyList()
                    Log.d("MenuLateralActivity", "Categorías obtenidas: $categorias") // Verifica las categorías obtenidas

                    if (categorias.isNotEmpty()) {
                        // Llenar el menú con las categorías obtenidas
                        cargarCategoriasEnMenuLateral(categorias)
                    } else {
                        Toast.makeText(this@MenuLateralActivity, "No se encontraron categorías", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MenuLateralActivity, "Error al obtener categorías", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                Toast.makeText(this@MenuLateralActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarCategoriasEnMenuLateral(categorias: List<Categoria>) {
        val menu = navigationView.menu // Obtenemos el menú desde el NavigationView

        // Obtenemos el ítem de categoría principal
        val navCategoria = menu.findItem(R.id.nav_categoria)

        // Obtenemos el submenú dentro de 'nav_categoria'
        val subMenu = navCategoria.subMenu

        // Limpiamos el submenú para evitar duplicados
        subMenu?.clear()

        // Verificamos que las categorías se han recibido correctamente
        if (categorias.isEmpty()) {
            Toast.makeText(this, "No se encontraron categorías", Toast.LENGTH_SHORT).show()
        }

        // Agregamos cada categoría al submenú
        categorias.forEach { categoria ->
            subMenu?.add(Menu.NONE, Menu.NONE, Menu.NONE, categoria.nombre)
                ?.setOnMenuItemClickListener {
                    // Aquí manejas el clic en la categoría
                    Toast.makeText(this, "Seleccionada: ${categoria.nombre}", Toast.LENGTH_SHORT).show()

                    // Redirigir a los productos de la categoría seleccionada
                    val intent = Intent(this, ProductosCategoriaActivity::class.java)
                    intent.putExtra("categoriaId", categoria.id) // Pasamos el ID de la categoría seleccionada
                    intent.putExtra("categoriaNombre", categoria.nombre) // Pasamos el nombre de la categoría
                    startActivity(intent)

                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
        }
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
