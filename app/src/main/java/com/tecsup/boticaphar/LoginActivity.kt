package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tecsup.boticaphar.models.LoginRequest
import com.tecsup.boticaphar.models.TokenResponse
import com.tecsup.boticaphar.models.UserData
import com.tecsup.boticaphar.network.RetrofitClientAuth
import com.tecsup.boticaphar.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.tecsup.boticaphar.network.ApiService

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.txt_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(username = email, password = password)
        val apiServiceAuth = RetrofitClientAuth.getApiServiceAuth()

        apiServiceAuth.loginUser(loginRequest).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    Log.d("LoginActivity", "Response: ${response.body()}")
                    val accessToken = tokenResponse?.access
                    val refreshToken = tokenResponse?.refresh

                    if (accessToken != null && refreshToken != null) {
                        saveTokens(accessToken, refreshToken)
                        Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                        // Ahora que tenemos el token, podemos obtener los datos del usuario
                        getUserData(accessToken)
                    } else {
                        Toast.makeText(this@LoginActivity, "Error al obtener el token.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginActivity", "Error: $errorBody")
                    if (!errorBody.isNullOrEmpty()) {
                        Toast.makeText(this@LoginActivity, "Error: $errorBody", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Guardar los tokens de acceso y refresco
    private fun saveTokens(accessToken: String, refreshToken: String) {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.apply()
    }

    // Obtener los datos del usuario después de iniciar sesión
    private fun getUserData(accessToken: String) {
        val apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        // Usamos el token de acceso en el encabezado de autorización
        val authHeader = "Bearer $accessToken"
        apiService.getUserData(authHeader).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    Log.d("LoginActivity", "User data: $userData")

                    // Aquí puedes acceder al ID del usuario y otros datos
                    val userId = userData?.id
                    saveUserId(userId)

                    // Navegar a la pantalla principal
                    navigateToHomeActivity()
                } else {
                    Toast.makeText(this@LoginActivity, "No se pudieron obtener los datos del usuario.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


    // Guardar el ID de usuario en SharedPreferences
    private fun saveUserId(userId: Int?) {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("user_id", userId ?: -1)  // Guardar un valor predeterminado si el ID es nulo
        editor.apply()
    }

    // Navegar a la actividad principal después de iniciar sesión
    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
