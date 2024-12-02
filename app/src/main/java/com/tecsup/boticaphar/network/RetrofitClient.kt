package com.tecsup.boticaphar.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://alexsandrovs.pythonanywhere.com/api/v1/"

    // Instancia de Retrofit que se inicializa solo una vez
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Función para obtener la instancia del Retrofit (sin necesidad de crearla de nuevo)
    fun getInstance(): Retrofit {
        return retrofit
    }

    // Instancia de ApiService utilizando la instancia de Retrofit
    val instance: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
