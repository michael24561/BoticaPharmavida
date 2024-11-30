package com.tecsup.boticaphar.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tecsup.boticaphar.DetalleProductoActivity
import com.tecsup.boticaphar.R
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.models.Carrito
import java.util.Locale

class ProductoAdapter(
    private var productos: List<Producto>,
    private val isHorizontal: Boolean = false
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    private var productosFiltrados: List<Producto> = productos.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val layoutRes = if (isHorizontal) R.layout.item_producto_horizontal else R.layout.item_producto
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ProductoViewHolder(view)
    }

    fun updateData(nuevosProductos: List<Producto>) {
        this.productos = nuevosProductos
        this.productosFiltrados = nuevosProductos.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productosFiltrados[position]
        holder.productoNombre.text = producto.nombre
        holder.productoPrecio.text = "$${producto.precio}"
        holder.productoDescripcion.text = producto.descripcion

        Picasso.get().load(producto.imagen).into(holder.productoImagen)

        val context = holder.itemView.context
        val sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""

        // Configurar clic en el botón "Agregar al Carrito"
        holder.agregarCarritoBtn.setOnClickListener {
            if (username.isNotEmpty()) {
                Carrito.agregarProducto(context, producto, username) // Pasar el username al método
                Toast.makeText(context, "${producto.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No se ha encontrado el usuario. Intenta nuevamente.", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar clic en el item para abrir la actividad de detalle
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetalleProductoActivity::class.java)
            intent.putExtra("producto", producto) // Pasar el producto seleccionado
            it.context.startActivity(intent)
        }
}

    override fun getItemCount(): Int = productosFiltrados.size

    fun filtrar(query: String) {
        val textoBusqueda = query.lowercase(Locale.getDefault())
        productosFiltrados = if (textoBusqueda.isEmpty()) {
            productos
        } else {
            productos.filter {
                it.nombre.lowercase(Locale.getDefault()).contains(textoBusqueda) ||
                        it.descripcion.lowercase(Locale.getDefault()).contains(textoBusqueda)
            }
        }
        notifyDataSetChanged()
    }

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productoNombre: TextView = itemView.findViewById(R.id.producto_nombre)
        val productoPrecio: TextView = itemView.findViewById(R.id.producto_precio)
        val productoImagen: ImageView = itemView.findViewById(R.id.producto_imagen)
        val productoDescripcion: TextView = itemView.findViewById(R.id.producto_descripcion)
        val agregarCarritoBtn: Button = itemView.findViewById(R.id.agregar_carrito_btn)
    }
}

