package com.tecsup.boticaphar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.boticaphar.R

class RecentSearchesAdapter(
    private val context: Context,
    private var busquedasRecientes: MutableList<String>,
    private val onSearchClickListener: (String) -> Unit,
    private val onDeleteClickListener: (String) -> Unit
) : RecyclerView.Adapter<RecentSearchesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchText: TextView = itemView.findViewById(R.id.search_text)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        init {
            deleteButton.setOnClickListener {
                val busqueda = busquedasRecientes[adapterPosition]
                onDeleteClickListener(busqueda)
            }

            itemView.setOnClickListener {
                val busqueda = busquedasRecientes[adapterPosition]
                onSearchClickListener(busqueda)
            }
        }
    }

    // Crear el ViewHolder para cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recent_search, parent, false)
        return ViewHolder(view)
    }

    // Enlazar los datos a la vista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val busqueda = busquedasRecientes[position]
        holder.searchText.text = busqueda
    }

    override fun getItemCount(): Int {
        return busquedasRecientes.size
    }

    // Actualizar la lista de búsquedas recientes
    fun setData(busquedas: MutableList<String>) {
        busquedasRecientes = busquedas
        notifyDataSetChanged()
    }
}
