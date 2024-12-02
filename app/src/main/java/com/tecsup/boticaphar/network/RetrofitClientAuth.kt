package com.tecsup.boticaphar.network

import android.content.Context
import android.content.SharedPreferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientAuth {
    private const val BASE_URL_AUTH = "https://alexsandrovs.pythonanywhere.com/api/"  // URL base para token

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_AUTH)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getInstance(): Retrofit {
        return retrofit
    }

    fun saveAuthToken(context: Context, accessToken: String?, refreshToken: String?) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.apply()
    }

    // Obtiene el token de SharedPreferences
    fun getAccessToken(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }

    // Obtiene el refresh token de SharedPreferences
    fun getRefreshToken(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("refresh_token", null)
    }
    fun getApiServiceAuth(): ApiServiceAuth {
        return retrofit.create(ApiServiceAuth::class.java)
    }
}
