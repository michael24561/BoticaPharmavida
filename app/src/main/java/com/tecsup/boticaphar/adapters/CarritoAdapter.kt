package com.tecsup.boticaphar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tecsup.boticaphar.R
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.utils.Carrito

class CarritoAdapter(
    private val productos: MutableList<Producto>, // Lista de productos del carrito
    private val onCarritoActualizado: () -> Unit // Callback para actualizar la vista
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = productos[position]

        // Configurar los datos del producto
        holder.productName.text = producto.nombre
        holder.productDescription.text = producto.descripcion
        holder.productPrice.text = "S/ ${producto.precio}"
        holder.productQuantity.text = "1" // Cantidad inicial

        // Calcular el precio total inicial
        holder.productTotalPrice.text = "Total: S/ ${producto.precio}"

        // Cargar imagen con Picasso
        Picasso.get().load(producto.imagen).into(holder.productImage)

        // Botón para eliminar producto
        holder.btnRemoveProduct.setOnClickListener {
            productos.removeAt(position) // Eliminar de la lista local
            Carrito.eliminarProducto(producto) // Eliminar del carrito global
            notifyItemRemoved(position)
            onCarritoActualizado() // Notificar que el carrito se actualizó
            Toast.makeText(holder.itemView.context, "${producto.nombre} eliminado del carrito", Toast.LENGTH_SHORT).show()
        }

        // Botón para aumentar cantidad
        holder.btnIncreaseQuantity.setOnClickListener {
            val cantidadActual = holder.productQuantity.text.toString().toInt()
            val nuevaCantidad = cantidadActual + 1
            holder.productQuantity.text = nuevaCantidad.toString()

            // Actualizar precio total
            val nuevoPrecioTotal = nuevaCantidad * producto.precio
            holder.productTotalPrice.text = "Total: S/ ${String.format("%.2f", nuevoPrecioTotal)}"
        }

        // Botón para disminuir cantidad
        holder.btnDecreaseQuantity.setOnClickListener {
            val cantidadActual = holder.productQuantity.text.toString().toInt()
            if (cantidadActual > 1) {
                val nuevaCantidad = cantidadActual - 1
                holder.productQuantity.text = nuevaCantidad.toString()

                // Actualizar precio total
                val nuevoPrecioTotal = nuevaCantidad * producto.precio
                holder.productTotalPrice.text = "Total: S/ ${String.format("%.2f", nuevoPrecioTotal)}"
            } else {
                Toast.makeText(holder.itemView.context, "Cantidad mínima alcanzada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = productos.size

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productDescription: TextView = itemView.findViewById(R.id.product_description)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val productQuantity: TextView = itemView.findViewById(R.id.product_quantity)
        val productTotalPrice: TextView = itemView.findViewById(R.id.product_total_price) // Precio total
        val btnIncreaseQuantity: ImageButton = itemView.findViewById(R.id.btn_increase_quantity)
        val btnDecreaseQuantity: ImageButton = itemView.findViewById(R.id.btn_decrease_quantity)
        val btnRemoveProduct: ImageButton = itemView.findViewById(R.id.btn_remove_product)
    }
}
