package com.tecsup.boticaphar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tecsup.boticaphar.R
import com.tecsup.boticaphar.models.Producto

class CarritoAdapter(
    private val productosCarrito: MutableList<Producto>
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productoImagen: ImageView = itemView.findViewById(R.id.product_image)
        val productoNombre: TextView = itemView.findViewById(R.id.product_name)
        val productoDescripcion: TextView = itemView.findViewById(R.id.product_description)
        val productoPrecio: TextView = itemView.findViewById(R.id.product_price)
        val botonEliminar: ImageButton = itemView.findViewById(R.id.btn_remove_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = productosCarrito[position]
        holder.productoNombre.text = producto.nombre
        holder.productoDescripcion.text = producto.descripcion
        holder.productoPrecio.text = "S/ ${producto.precio}"

        // Cargar imagen con Picasso
        Picasso.get().load(producto.imagen).into(holder.productoImagen)

        // Eliminar producto del carrito
        holder.botonEliminar.setOnClickListener {
            productosCarrito.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = productosCarrito.size
}
