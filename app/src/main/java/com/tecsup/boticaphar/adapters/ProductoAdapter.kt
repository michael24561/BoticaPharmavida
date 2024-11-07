package com.tecsup.boticaphar.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.tecsup.boticaphar.R
import com.tecsup.boticaphar.models.Producto

class ProductoAdapter(private val productos: List<Producto>) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.productoNombre.text = producto.nombre
        holder.productoPrecio.text = "Precio: ${producto.precio}"

        // Usando Picasso para cargar la imagen desde la URL dinámica
        Picasso.get()
            .load(producto.imagen)  // Aquí usas el campo 'imagen' de tu objeto Producto
            .into(holder.productoImagen)
    }

    override fun getItemCount(): Int = productos.size

    class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productoNombre: TextView = view.findViewById(R.id.producto_nombre)
        val productoPrecio: TextView = view.findViewById(R.id.producto_precio)
        val productoImagen: ImageView = view.findViewById(R.id.producto_imagen)
    }
}





