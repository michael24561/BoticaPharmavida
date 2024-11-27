package com.tecsup.boticaphar.network

import com.tecsup.boticaphar.models.LoginRequest
import com.tecsup.boticaphar.models.TokenResponse
import com.tecsup.boticaphar.models.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceAuth {
    @POST("token/")  // Endpoint para obtener el token
    fun loginUser(@Body loginRequest: LoginRequest): Call<TokenResponse>
}