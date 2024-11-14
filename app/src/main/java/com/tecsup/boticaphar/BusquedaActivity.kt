package com.tecsup.boticaphar

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import com.tecsup.boticaphar.adapters.RecentSearchesAdapter
import com.tecsup.boticaphar.adapters.ProductoHorizontalAdapter
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.network.ApiService
import com.tecsup.boticaphar.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusquedaActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        RetrofitClient.getInstance().create(ApiService::class.java)
    }

    private var productos: List<Producto> = emptyList()
    private lateinit var recentSearchesAdapter: RecentSearchesAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var busquedasRecientes: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)

        val searchInput: EditText = findViewById(R.id.search_bar)
        val recyclerViewProductos: RecyclerView = findViewById(R.id.recycler_view_productos)
        val recyclerViewRecientes: RecyclerView = findViewById(R.id.recent_searches_recycler_view)
        val containerRecientes: LinearLayout = findViewById(R.id.container_recientes)
        val containerProductos: LinearLayout = findViewById(R.id.container_productos)
        val cantidadProductosTextView: TextView = findViewById(R.id.resultado_busqueda_titulo) // El TextView para mostrar la cantidad

        // Configurar RecyclerView de productos (vertical)
        recyclerViewProductos.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter =
            ProductoHorizontalAdapter(emptyList())  // Inicializar el adaptador con lista vacía
        recyclerViewProductos.adapter = adapter

        // Inicializar SharedPreferences y obtener las búsquedas recientes
        sharedPreferences = getSharedPreferences("historial_busquedas", MODE_PRIVATE)
        busquedasRecientes = cargarBusquedasRecientes()

        // Configurar RecyclerView de búsquedas recientes
        recyclerViewRecientes.layoutManager = LinearLayoutManager(this)

        // Inicializar el adaptador para búsquedas recientes
        recentSearchesAdapter = RecentSearchesAdapter(
            context = this,
            busquedasRecientes = busquedasRecientes,
            onDeleteClickListener = { busqueda -> eliminarBusquedaReciente(busqueda) },
            onSearchClickListener = { busqueda ->
                // Colocar la búsqueda en la barra de búsqueda y realizar la búsqueda automáticamente
                searchInput.setText(busqueda)
                realizarBusqueda(busqueda)  // Realiza la búsqueda
                containerRecientes.visibility = LinearLayout.GONE  // Ocultar las búsquedas recientes
                containerProductos.visibility = LinearLayout.VISIBLE  // Mostrar los productos
            }
        )
        recyclerViewRecientes.adapter = recentSearchesAdapter

        // Mostrar las búsquedas recientes
        if (busquedasRecientes.isNotEmpty()) {
            containerRecientes.visibility = LinearLayout.VISIBLE
            recentSearchesAdapter.setData(busquedasRecientes)
        } else {
            containerRecientes.visibility = LinearLayout.GONE
        }

        // Configurar el evento de Enter en la barra de búsqueda
        searchInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val query = searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    realizarBusqueda(query) // Llamar a realizar búsqueda con el texto ingresado
                    guardarBusquedaReciente(query) // Guardar la búsqueda reciente
                    ocultarTeclado() // Cerrar el teclado
                    containerRecientes.visibility = LinearLayout.GONE // Ocultar búsquedas recientes
                    containerProductos.visibility = LinearLayout.VISIBLE // Mostrar productos
                } else {
                    Toast.makeText(
                        this,
                        "Por favor ingrese un término de búsqueda",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            } else {
                false
            }
        }
    }

    // Método para realizar la búsqueda
    private fun realizarBusqueda(query: String) {
        if (TextUtils.isEmpty(query)) {
            mostrarProductos(emptyList())  // Si la búsqueda está vacía, mostrar lista vacía
        } else {
            val call = apiService.obtenerProductos()  // Llamada a la API para obtener productos
            call.enqueue(object : Callback<List<Producto>> {
                override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                    if (response.isSuccessful) {
                        val productosRecibidos = response.body()
                        if (productosRecibidos != null && productosRecibidos.isNotEmpty()) {
                            // Filtrar productos que coincidan con la búsqueda
                            productos = productosRecibidos.filter {
                                it.nombre.contains(query, ignoreCase = true)  // Filtrar por nombre
                            }

                            // Mostrar productos encontrados
                            mostrarProductos(productos)

                            // Actualizar el número de productos en el TextView
                            val cantidadProductos = productos.size
                            val cantidadProductosTextView: TextView = findViewById(R.id.resultado_busqueda_titulo)
                            cantidadProductosTextView.text = "Resultado de Búsqueda: $cantidadProductos productos encontrados"
                        } else {
                            Toast.makeText(this@BusquedaActivity, "No se encontraron productos", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@BusquedaActivity, "Error al realizar la búsqueda", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                    Toast.makeText(this@BusquedaActivity, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun mostrarProductos(productos: List<Producto>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_productos)
        val adapter = ProductoHorizontalAdapter(productos)  // Usar adaptador con productos filtrados
        recyclerView.adapter = adapter
    }

    private fun ocultarTeclado() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun guardarBusquedaReciente(busqueda: String) {
        if (!busquedasRecientes.contains(busqueda)) {
            busquedasRecientes.add(0, busqueda)  // Agregar la búsqueda al inicio de la lista
            if (busquedasRecientes.size > 5) {
                busquedasRecientes.removeAt(busquedasRecientes.size - 1)  // Mantener un máximo de 5 búsquedas
            }

            val editor = sharedPreferences.edit()
            editor.putStringSet("busquedas", busquedasRecientes.toSet())
            editor.apply()

            recentSearchesAdapter.setData(busquedasRecientes)  // Actualizar el adaptador de búsquedas recientes
        }
    }

    private fun cargarBusquedasRecientes(): MutableList<String> {
        val busquedas = sharedPreferences.getStringSet("busquedas", mutableSetOf()) ?: mutableSetOf()
        return busquedas.toMutableList()
    }

    private fun eliminarBusquedaReciente(busqueda: String) {
        if (busquedasRecientes.remove(busqueda)) {  // Remover la búsqueda y verificar si se eliminó
            val editor = sharedPreferences.edit()
            editor.putStringSet("busquedas", busquedasRecientes.toSet())
            editor.apply()
            recentSearchesAdapter.setData(busquedasRecientes)  // Actualizar el adaptador

            // Mostrar u ocultar el contenedor de búsquedas recientes según la lista
            val containerRecientes: LinearLayout = findViewById(R.id.container_recientes)
            containerRecientes.visibility = if (busquedasRecientes.isEmpty()) LinearLayout.GONE else LinearLayout.VISIBLE
        }
    }
}



