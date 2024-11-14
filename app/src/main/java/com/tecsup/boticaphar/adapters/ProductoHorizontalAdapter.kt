package com.tecsup.boticaphar.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tecsup.boticaphar.DetalleProductoActivity
import com.tecsup.boticaphar.R
import com.tecsup.boticaphar.models.Producto
import java.util.Locale

class ProductoHorizontalAdapter(
    private var productos: List<Producto>
) : RecyclerView.Adapter<ProductoHorizontalAdapter.ProductoViewHolder>() {

    private var productosFiltrados: List<Producto> = productos.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto_horizontal, parent, false)
        return ProductoViewHolder(view)
    }

    fun updateData(nuevosProductos: List<Producto>) {
        this.productos = nuevosProductos
        notifyDataSetChanged() // Esto notifica al RecyclerView que los datos han cambiado
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productosFiltrados[position]
        holder.productoNombre.text = producto.nombre
        holder.productoPrecio.text = "$${producto.precio}"

        // Cargar la imagen usando Picasso
        Picasso.get().load(producto.imagen).into(holder.productoImagen)

        // Configurar el clic para abrir la actividad de detalle
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetalleProductoActivity::class.java)
            intent.putExtra("producto", producto) // Pasar el producto seleccionado
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productosFiltrados.size

    // Método para filtrar productos según el texto ingresado
    fun filtrar(query: String) {
        val textoBusqueda = query.lowercase(Locale.getDefault())
        productosFiltrados = if (textoBusqueda.isEmpty()) {
            productos
        } else {
            productos.filter { producto ->
                producto.nombre.lowercase(Locale.getDefault()).contains(textoBusqueda) ||
                        producto.descripcion.lowercase(Locale.getDefault()).contains(textoBusqueda)
            }
        }
        notifyDataSetChanged()
    }

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productoNombre: TextView = itemView.findViewById(R.id.producto_nombre)
        val productoPrecio: TextView = itemView.findViewById(R.id.producto_precio)
        val productoImagen: ImageView = itemView.findViewById(R.id.producto_imagen)
    }
}
