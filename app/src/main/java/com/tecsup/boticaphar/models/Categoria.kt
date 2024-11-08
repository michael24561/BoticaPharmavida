package com.tecsup.boticaphar.models


data class Categoria(
    val id: Int,
    val nombre: String,
    var productos: List<Producto> = listOf()
)
