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

        if (!accessToken.isNullOrEmpty()) {
            // Si ya hay un token, ir directamente al HomeActivity
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()  // Termina LoginActivity para evitar que el usuario regrese a ella
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

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

                    if (accessToken != null && refreshToken != null) {
                        saveTokens(accessToken, refreshToken, email)  // Guardamos el username también
                        Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Error al obtener el token.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    when (response.code()) {
                        400 -> Toast.makeText(this@LoginActivity, "Credenciales incorrectas. Verifica tu correo y contraseña.", Toast.LENGTH_SHORT).show()
                        401 -> Toast.makeText(this@LoginActivity, "No autorizado. Revisa tus datos de acceso.", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(this@LoginActivity, "Error: ${response.code()}. Intenta nuevamente.", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveTokens(accessToken: String, refreshToken: String, username: String) {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.putString("username", username)  // Guardamos el username
        editor.apply()
    }
}
