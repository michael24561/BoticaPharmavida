package com.tecsup.boticaphar.adapters

import android.content.Context
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
import com.tecsup.boticaphar.models.Carrito

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
        val context = holder.itemView.context

        // Obtener el username de SharedPreferences o de otro lugar adecuado
        val sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""  // Aquí tomamos el username guardado

        // Configurar los datos del producto
        holder.productName.text = producto.nombre
        holder.productDescription.text = producto.descripcion
        holder.productPrice.text = "S/ ${producto.precio}"
        holder.productQuantity.text = producto.cantidad.toString() // Mostrar la cantidad desde el modelo

        // Calcular el precio total inicial
        holder.productTotalPrice.text = "Total: S/ ${String.format("%.2f", producto.precio * producto.cantidad)}"

        // Cargar imagen con Picasso
        Picasso.get().load(producto.imagen).into(holder.productImage)

        // Botón para eliminar producto
        holder.btnRemoveProduct.setOnClickListener {
            productos.removeAt(position) // Eliminar de la lista local
            Carrito.eliminarProducto(context, producto, username) // Pasar el username
            notifyItemRemoved(position)
            onCarritoActualizado() // Notificar que el carrito se actualizó
            Toast.makeText(context, "${producto.nombre} eliminado del carrito", Toast.LENGTH_SHORT).show()
        }

        // Botón para aumentar cantidad
        holder.btnIncreaseQuantity.setOnClickListener {
            val nuevaCantidad = producto.cantidad + 1
            producto.cantidad = nuevaCantidad // Actualizar la cantidad en el modelo

            holder.productQuantity.text = nuevaCantidad.toString() // Actualizar la vista

            // Actualizar precio total
            val nuevoPrecioTotal = nuevaCantidad * producto.precio
            holder.productTotalPrice.text = "Total: S/ ${String.format("%.2f", nuevoPrecioTotal)}"

            // Actualizar carrito global
            Carrito.actualizarCantidad(context, producto, nuevaCantidad, username) // Pasar el username
            onCarritoActualizado() // Notificar que el carrito se actualizó
        }

        // Botón para disminuir cantidad
        holder.btnDecreaseQuantity.setOnClickListener {
            val cantidadActual = producto.cantidad
            if (cantidadActual > 1) {
                val nuevaCantidad = cantidadActual - 1
                producto.cantidad = nuevaCantidad // Actualizar la cantidad en el modelo

                holder.productQuantity.text = nuevaCantidad.toString() // Actualizar la vista

                // Actualizar precio total
                val nuevoPrecioTotal = nuevaCantidad * producto.precio
                holder.productTotalPrice.text = "Total: S/ ${String.format("%.2f", nuevoPrecioTotal)}"

                // Actualizar carrito global
                Carrito.actualizarCantidad(context, producto, nuevaCantidad, username) // Pasar el username
                onCarritoActualizado() // Notificar que el carrito se actualizó
            } else {
                Toast.makeText(context, "Cantidad mínima alcanzada", Toast.LENGTH_SHORT).show()
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
