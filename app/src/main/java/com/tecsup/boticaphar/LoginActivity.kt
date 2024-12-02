package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

    // Método para manejar clic en el TextView de registro
    fun onRegisterClick(view: View) {
        Log.d("LoginActivity", "El usuario hizo clic en 'Crea una cuenta'. Redirigiendo a RegisterActivity.")
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    // Verifica si ya hay un token guardado
    private fun checkIfUserIsLoggedIn() {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("access_token", null)

        Log.d("LoginActivity", "Comprobando si el usuario está logueado. Token almacenado: $accessToken")

        if (!accessToken.isNullOrEmpty()) {
            Log.d("LoginActivity", "Token encontrado. Redirigiendo a HomeActivity.")
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
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
                        saveTokens(accessToken, refreshToken, email)
                        getCurrentUser(accessToken)
                    } else {
                        Log.e("LoginActivity", "Error: El token de acceso o refresh es nulo.")
                        Toast.makeText(this@LoginActivity, "Error al obtener el token.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("LoginActivity", "Error al hacer login. Código: ${response.code()} Respuesta: ${response.errorBody()?.string()}")
                    handleLoginError(response.code())
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.e("LoginActivity", "Error de conexión: ${t.message}", t)
                Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun handleLoginError(errorCode: Int) {
        Log.d("LoginActivity", "Manejando error de login. Código de error: $errorCode")
        when (errorCode) {
            400 -> Toast.makeText(this@LoginActivity, "Credenciales incorrectas. Verifica tu correo y contraseña.", Toast.LENGTH_SHORT).show()
            401 -> Toast.makeText(this@LoginActivity, "No autorizado. Revisa tus datos de acceso.", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this@LoginActivity, "Error: $errorCode. Intenta nuevamente.", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveTokens(accessToken: String, refreshToken: String, username: String) {
        Log.d("LoginActivity", "Guardando tokens en SharedPreferences...")
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.putString("username", username)
        editor.apply()

        Log.d("LoginActivity", "Tokens guardados correctamente: Access Token: $accessToken, Refresh Token: $refreshToken")
    }

    private fun getCurrentUser(accessToken: String) {
        val apiService = RetrofitClient.getApiService()
        val bearerToken = "Bearer $accessToken"

        Log.d("LoginActivity", "Obteniendo datos del usuario con token: $bearerToken")

        apiService.getCurrentUser(bearerToken).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    Log.d("LoginActivity", "Respuesta del servidor: ${response.body()}")

                    if (userData != null && userData.cliente_id != null) {
                        Log.d("LoginActivity", "ID del usuario obtenido: ${userData.cliente_id}")
                        saveClientId(userData.cliente_id)
                        Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Log.e("LoginActivity", "Error: ID del usuario es nulo o vacío.")
                        Toast.makeText(this@LoginActivity, "Error al obtener el ID del usuario.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginActivity", "Error al obtener datos del usuario. Código: ${response.code()} Respuesta: ${response.errorBody()?.string()}")
                    Toast.makeText(this@LoginActivity, "Error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("LoginActivity", "Error de conexión al obtener datos del usuario: ${t.message}", t)
                Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveClientId(clientId: Int) {
        Log.d("LoginActivity", "Guardando el ID del cliente en SharedPreferences...")
        val sharedPreferences = getSharedPreferences("authPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("cliente_id", clientId)
        editor.apply()

        val savedId = sharedPreferences.getInt("cliente_id", -1)
        Log.d("LoginActivity", "Verificación de almacenamiento de ID del cliente: $savedId")
    }
}
