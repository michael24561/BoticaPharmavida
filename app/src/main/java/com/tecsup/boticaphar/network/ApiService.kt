package com.tecsup.boticaphar.network

import com.tecsup.boticaphar.models.Categoria
import com.tecsup.boticaphar.models.Producto
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET("categorias/")
    fun obtenerCategorias(): Call<List<Categoria>>

    @GET("productos/")
    fun obtenerProductos(): Call<List<Producto>>
}
