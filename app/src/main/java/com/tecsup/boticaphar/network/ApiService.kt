package com.tecsup.boticaphar.network

import com.tecsup.boticaphar.models.Categoria
import com.tecsup.boticaphar.models.Producto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("categorias/")  // Endpoint para obtener todas las categorías
    fun obtenerCategorias(): Call<List<Categoria>>

    @GET("productos/")  // Endpoint para obtener todos los productos (sin filtrar por categoría)
    fun obtenerProductos(): Call<List<Producto>>

    @GET("productos/categoria/{categoria_id}/")
    fun obtenerProductosPorCategoria(@Path("categoria_id") categoriaId: Int): Call<List<Producto>>
}
