package com.tecsup.boticaphar.network

import com.tecsup.boticaphar.models.Categoria
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.models.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("categorias/")  // Endpoint para obtener todas las categorías
    fun obtenerCategorias(): Call<List<Categoria>>

    @GET("productos/")  // Endpoint para obtener todos los productos (sin filtrar por categoría)
    fun obtenerProductos(): Call<List<Producto>>

    @GET("productos/categoria/{categoria_id}/")
    fun obtenerProductosPorCategoria(@Path("categoria_id") categoriaId: Int): Call<List<Producto>>

    @POST("clientes/")
    fun registerUser(@Body userData: UserData): Call<Void>

    @GET("clientes/{id}/")
    fun getUserData(@Path("id") userId: Int): Call<UserData>

    @GET("clientes/") // Endpoint para obtener datos del usuario autenticado
    fun getUserData(@Header("Authorization") authHeader: String): Call<UserData>

    // Actualizar datos de un usuario específico
    @PUT("clientes/{id}/")
    fun updateCliente(
        @Path("id") id: Int,
        @Body cliente: UserData
    ): Call<UserData>


    @PATCH("clientes/{id}/")
    fun patchClient(@Path("id") clientId: Int, @Body clientData: Map<String, Any>): Call<UserData>


}

