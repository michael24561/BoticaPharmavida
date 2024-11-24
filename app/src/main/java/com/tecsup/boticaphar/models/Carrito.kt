package com.tecsup.boticaphar.utils

import com.tecsup.boticaphar.models.Producto

object Carrito {
    private val productosEnCarrito = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productosEnCarrito.add(producto)
    }

    fun obtenerProductos(): List<Producto> {
        return productosEnCarrito
    }

    fun eliminarProducto(producto: Producto) {
        productosEnCarrito.remove(producto)
    }

    fun vaciarCarrito() {
        productosEnCarrito.clear()
    }
}
