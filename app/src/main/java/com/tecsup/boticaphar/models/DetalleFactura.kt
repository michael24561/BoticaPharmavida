package com.tecsup.boticaphar.models

data class DetalleFactura(
    val producto: String,
    val cantidad: Int,
    val precio_unitario: String,
    val subtotal: String
)