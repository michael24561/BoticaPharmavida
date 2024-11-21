package com.tecsup.boticaphar.network

import android.content.Context
import com.tecsup.boticaphar.models.LoginRequest
import com.tecsup.boticaphar.models.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService(private val context: Context) {

    private val apiServiceAuth = RetrofitClientAuth.getInstance().create(ApiServiceAuth::class.java)

    fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        apiServiceAuth.loginUser(loginRequest).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    val accessToken = tokenResponse?.access
                    val refreshToken = tokenResponse?.refresh

                    // Guarda el token
                    RetrofitClientAuth.saveAuthToken(context, accessToken, refreshToken)

                    // Imprime los tokens
                    println("Access Token: $accessToken")
                    println("Refresh Token: $refreshToken")
                } else {
                    // Maneja el error (por ejemplo, credenciales incorrectas)
                    println("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                // Maneja la falla (por ejemplo, problemas de conexión)
                t.printStackTrace()
            }
        })
    }
}
