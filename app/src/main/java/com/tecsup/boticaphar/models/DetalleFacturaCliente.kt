package com.tecsup.boticaphar.models

data class DetalleFacturaCliente(
    val producto: Int,  // Usar el ID del producto
    val cantidad: Int,
    val precio_unitario: Double,
    val subtotal: Double
)