package com.tecsup.boticaphar.models

data class Pago(
    val cliente: String,
    val fecha: String,
    val subtotal: Double,
    val igv: Double,
    val total: Double,
    val detalles: List<Detalle>
)