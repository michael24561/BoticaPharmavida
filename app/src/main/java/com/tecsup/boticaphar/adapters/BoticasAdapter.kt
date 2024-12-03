package com.tecsup.boticaphar.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.boticaphar.BoticasListActivity
import com.tecsup.boticaphar.R
import com.tecsup.boticaphar.models.Botica

class BoticasAdapter(
    private val boticasList: List<Botica>,
    private val context: Context
) : RecyclerView.Adapter<BoticasAdapter.BoticaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoticaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_botica, parent, false)
        return BoticaViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoticaViewHolder, position: Int) {
        val botica = boticasList[position]
        holder.nombre.text = botica.nombre
        holder.direccion.text = botica.direccion

        holder.itemView.setOnClickListener {
            // Pasar datos seleccionados de la botica
            val intent = Intent()
            intent.putExtra("nombreBotica", botica.nombre)
            intent.putExtra("direccionBotica", botica.direccion)
            if (context is BoticasListActivity) {
                context.setResult(AppCompatActivity.RESULT_OK, intent)
                context.finish()
            }
        }
    }

    override fun getItemCount(): Int = boticasList.size

    inner class BoticaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombreBotica)
        val direccion: TextView = itemView.findViewById(R.id.direccionBotica)
    }
}
