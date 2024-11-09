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

class ProductoAdapter(
    private val productos: List<Producto>,
    private val isProductosSimilares: Boolean = false
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.productoNombre.text = producto.nombre
        holder.productoPrecio.text = "$${producto.precio}"
        holder.productoDescripcion.text = producto.descripcion

        // Cargar la imagen usando Picasso
        Picasso.get().load(producto.imagen).into(holder.productoImagen)

        // Configurar el clic para abrir la actividad de detalle
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetalleProductoActivity::class.java)
            intent.putExtra("producto", producto) // Pasar el producto seleccionado
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productos.size

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productoNombre: TextView = itemView.findViewById(R.id.producto_nombre)
        val productoPrecio: TextView = itemView.findViewById(R.id.producto_precio)
        val productoImagen: ImageView = itemView.findViewById(R.id.producto_imagen)
        val productoDescripcion: TextView = itemView.findViewById(R.id.producto_descripcion)
    }
}

