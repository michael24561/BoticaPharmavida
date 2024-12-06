package com.tecsup.boticaphar.models

data class Detalle(
    val producto: String,
    val cantidad: Int,
    val precio_unitario: Double,
    val subtotal: Double
)