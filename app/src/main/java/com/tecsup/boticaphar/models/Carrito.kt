package com.tecsup.boticaphar.models

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Carrito {
    private const val PREFS_NAME = "carrito_prefs"
    private const val KEY_PRODUCTOS = "productos"

    // Obtener SharedPreferences
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Guardar productos en SharedPreferences
    private fun guardarProductos(context: Context, productos: List<Producto>) {
        val gson = Gson()
        val productosJson = gson.toJson(productos)
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_PRODUCTOS, productosJson)
        editor.apply()
    }

    // Obtener productos desde SharedPreferences
    fun obtenerProductos(context: Context): List<Producto> {
        val gson = Gson()
        val productosJson = getSharedPreferences(context).getString(KEY_PRODUCTOS, "[]")
        val type = object : TypeToken<List<Producto>>() {}.type
        return gson.fromJson(productosJson, type)
    }

    // Agregar un producto al carrito
    fun agregarProducto(context: Context, producto: Producto) {
        val productos = obtenerProductos(context).toMutableList()

        // Verificar si el producto ya existe en el carrito y actualizar su cantidad
        val index = productos.indexOfFirst { it.id == producto.id }
        if (index != -1) {
            productos[index].cantidad += producto.cantidad  // Incrementar la cantidad
        } else {
            producto.cantidad = 1
            productos.add(producto)  // Si no existe, agregar el producto
        }

        guardarProductos(context, productos)
    }


    // Eliminar un producto del carrito
    fun eliminarProducto(context: Context, producto: Producto) {
        val productos = obtenerProductos(context).toMutableList()
        productos.removeAll { it.id == producto.id }
        guardarProductos(context, productos)
    }


    // Actualizar la cantidad de un producto en el carrito
    fun actualizarCantidad(context: Context, producto: Producto, nuevaCantidad: Int) {
        val productos = obtenerProductos(context).toMutableList()
        val index = productos.indexOfFirst { it.id == producto.id }

        if (index != -1) {
            // Si el producto está en el carrito, actualizamos la cantidad
            productos[index].cantidad = nuevaCantidad
            guardarProductos(context, productos)
        }

        // Vaciar el carrito
        fun vaciarCarrito(context: Context) {
            val editor = getSharedPreferences(context).edit()
            editor.putString(KEY_PRODUCTOS, "[]") // Establecer una lista vacía
            editor.apply()
        }
    }
}
