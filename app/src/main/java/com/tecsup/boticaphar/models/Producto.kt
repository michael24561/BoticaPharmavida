package com.tecsup.boticaphar.models

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val presentacion: String,
    val fechaVencimiento: String,
    val proveedorId: Int,
    val categoriaId: Int,
    val imagen: String,
    val stock: Int,
    val precio: Double
)