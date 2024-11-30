package com.tecsup.boticaphar.network

import com.tecsup.boticaphar.models.Categoria
import com.tecsup.boticaphar.models.Pedido
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.models.UserData
import retrofit2.Call
import retrofit2.Response
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
    @GET("categorias/")
    fun obtenerCategorias(): Call<List<Categoria>>

    @GET("productos/")
    fun obtenerProductos(): Call<List<Producto>>

    @GET("productos/categoria/{categoria_id}/")
    fun obtenerProductosPorCategoria(@Path("categoria_id") categoriaId: Int): Call<List<Producto>>

    @POST("clientes/")
    fun registerUser(@Body userData: UserData): Call<UserData>

    @GET("clientes/{id}/")
    fun getUserData(@Path("id") userId: Int): Call<UserData>

    @GET("clientes/")
    fun getUserData(@Header("Authorization") authHeader: String): Call<UserData>

    @PUT("clientes/{id}/")
    fun updateUserProfile(@Path("id") userId: Int, @Body userData: UserData): Call<UserData>


    @PATCH("clientes/{id}/")
    fun patchClient(@Path("id") clientId: Int, @Body clientData: Map<String, Any>): Call<UserData>

    @POST("pedidos/")
    fun realizarPedido(@Body pedido: Pedido): Call<Void>



}

