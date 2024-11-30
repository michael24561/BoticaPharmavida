package com.tecsup.boticaphar.models

data class Pedido(
    val id: Int,
    val fecha_pedido: String,
    val total_pedido: Double,
    val estado: String,
    val cantidad: Int,
    val precio_compra: Double,
    val proveedor: Int,
    val producto: Int
)
