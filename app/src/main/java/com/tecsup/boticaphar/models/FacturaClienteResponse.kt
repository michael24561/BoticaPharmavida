package com.tecsup.boticaphar.models

data class FacturaClienteResponse(
    val id: Int,
    val cliente: String,
    val fecha: String,
    val subtotal: String,
    val igv: String,
    val total: String,
    val detalles: List<DetalleFactura>
)