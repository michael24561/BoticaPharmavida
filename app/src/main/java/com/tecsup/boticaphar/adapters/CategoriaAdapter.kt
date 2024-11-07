package com.tecsup.boticaphar.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.boticaphar.R
import com.tecsup.boticaphar.models.Categoria
import com.tecsup.boticaphar.models.Producto

class CategoriaAdapter(private val categorias: List<Categoria>) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categorias[position]
        holder.categoriaNombre.text = categoria.nombre
        // Aquí puedes manejar cualquier otra propiedad de la categoría

        // Establecer el adaptador para los productos dentro de la categoría
        val productoAdapter = ProductoAdapter(categoria.productos)
        holder.productosRecyclerView.adapter = productoAdapter
    }

    override fun getItemCount(): Int = categorias.size

    class CategoriaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoriaNombre: TextView = view.findViewById(R.id.categoria_nombre)
        val productosRecyclerView: RecyclerView = view.findViewById(R.id.productos_recycler_view)
    }
}





