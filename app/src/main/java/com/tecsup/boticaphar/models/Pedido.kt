package com.tecsup.boticaphar.models

import com.google.gson.annotations.SerializedName

data class Pedido(
    val id: Int,
    val fecha_pedido: String,
    val total_pedido: Double,
    val estado: String,
    val cantidad: Int,
    val precio_compra: Double,
    @SerializedName("producto_id") val productoId: Int, // Asegúrate de que sea "producto_id"
    @SerializedName("proveedor_id") val proveedorId: Int // Lo mismo para proveedor_id
)


