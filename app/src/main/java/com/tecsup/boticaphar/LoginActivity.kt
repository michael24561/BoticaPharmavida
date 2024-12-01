package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tecsup.boticaphar.models.LoginRequest
import com.tecsup.boticaphar.models.TokenResponse
import com.tecsup.boticaphar.models.UserData
import com.tecsup.boticaphar.network.RetrofitClient
import com.tecsup.boticaphar.network.RetrofitClientAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Comprobamos si ya hay un token almacenado
        checkIfUserIsLoggedIn()

        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.txt_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener {
            loginUser()
        }
    }

    // Verifica si ya hay un token guardado
    private fun checkIfUserIsLoggedIn() {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("access_token", null)

        Log.d("LoginActivity", "Comprobando si el usuario está logueado. Token: $accessToken")

        if (!accessToken.isNullOrEmpty()) {
            // Si ya hay un token, ir directamente al HomeActivity
            Log.d("LoginActivity", "Token encontrado. Redirigiendo a HomeActivity.")
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()  // Termina LoginActivity para evitar que el usuario regrese a ella
        } else {
            Log.d("LoginActivity", "No se encontró token. Continuar con el login.")
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        Log.d("LoginActivity", "Intentando iniciar sesión con email: $email")

        if (email.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el correo.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa la contraseña.", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(username = email, password = password)
        val apiServiceAuth = RetrofitClientAuth.getApiServiceAuth()

        apiServiceAuth.loginUser(loginRequest).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    val accessToken = tokenResponse?.access
                    val refreshToken = tokenResponse?.refresh

                    Log.d("LoginActivity", "Respuesta exitosa de login. Acceso: $accessToken, Refresh: $refreshToken")

                    if (accessToken != null && refreshToken != null) {
                        saveTokens(accessToken, refreshToken, email)  // Guardamos el access_token, refresh_token y username
                        getCurrentUser(accessToken)  // Obtenemos el id del usuario
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error al obtener el token.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.d("LoginActivity", "Error al hacer login. Código: ${response.code()}")
                    handleLoginError(response.code())
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.e("LoginActivity", "Error de conexión: ${t.message}")
                Toast.makeText(
                    this@LoginActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun handleLoginError(errorCode: Int) {
        when (errorCode) {
            400 -> Toast.makeText(
                this@LoginActivity,
                "Credenciales incorrectas. Verifica tu correo y contraseña.",
                Toast.LENGTH_SHORT
            ).show()

            401 -> Toast.makeText(
                this@LoginActivity,
                "No autorizado. Revisa tus datos de acceso.",
                Toast.LENGTH_SHORT
            ).show()

            else -> Toast.makeText(
                this@LoginActivity,
                "Error: $errorCode. Intenta nuevamente.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun saveTokens(accessToken: String, refreshToken: String, username: String) {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.putString("username", username)  // Guardamos el username
        editor.apply()

        Log.d("LoginActivity", "Tokens guardados: Access Token: $accessToken, Refresh Token: $refreshToken")
    }

    private fun getCurrentUser(accessToken: String) {
        val apiService = RetrofitClient.getApiService()
        val bearerToken = "Bearer $accessToken"

        apiService.getCurrentUser(bearerToken).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    Log.d("LoginActivity", "Respuesta del servidor: ${response.body()}")

                    if (userData != null && userData.cliente_id != null) {
                        Log.d("LoginActivity", "ID del usuario obtenido: ${userData.cliente_id}")
                        saveClientId(userData.cliente_id)  // Guardamos solo el ID del cliente
                        Toast.makeText(
                            this@LoginActivity,
                            "Inicio de sesión exitoso.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Log.e("LoginActivity", "ID del usuario es nulo o vacío")
                        Toast.makeText(
                            this@LoginActivity,
                            "Error al obtener el ID del usuario.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("LoginActivity", "Error en la respuesta al obtener datos del usuario: ${response.code()}")
                    Toast.makeText(
                        this@LoginActivity,
                        "Error al obtener los datos del usuario.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("LoginActivity", "Error de conexión: ${t.message}", t)
                Toast.makeText(
                    this@LoginActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun saveClientId(clientId: Int) {
        val sharedPreferences = getSharedPreferences("authPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("cliente_id", clientId)  // Guardamos el ID del cliente
        editor.apply()

        // Verificamos si se guardó correctamente
        val savedId = sharedPreferences.getInt("cliente_id", -1)
        Log.d("LoginActivity", "ID del cliente guardado: $savedId")
    }
}
