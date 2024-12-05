package com.tecsup.boticaphar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.boticaphar.R
import com.tecsup.boticaphar.models.Producto

class FacturaAdapter(private val productos: List<Producto>) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_factura, parent, false)
        return FacturaViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombreProducto.text = producto.nombre
        holder.cantidad.text = "Cantidad: ${producto.cantidad}"
        holder.precioUnitario.text = "Precio Unitario: S/ ${String.format("%.2f", producto.precioUnitario)}"
        holder.precioTotal.text = "Precio Total: S/ ${String.format("%.2f", producto.precioUnitario * producto.cantidad)}"
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    class FacturaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProducto: TextView = itemView.findViewById(R.id.nombre_producto)
        val cantidad: TextView = itemView.findViewById(R.id.cantidad_producto)
        val precioUnitario: TextView = itemView.findViewById(R.id.precio_unitario)
        val precioTotal: TextView = itemView.findViewById(R.id.precio_total)
    }
}
