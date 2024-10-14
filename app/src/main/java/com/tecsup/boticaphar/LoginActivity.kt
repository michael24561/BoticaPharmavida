package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.txt_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val tvRegisterPrompt = findViewById<TextView>(R.id.tv_register_prompt)

        btnLogin.setOnClickListener {
            loginUser()
        }

        tvRegisterPrompt.setOnClickListener {
            // Redirigir a la actividad de registro
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Recuperar datos guardados en SharedPreferences
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)

        if (email == savedEmail && password == savedPassword) {
            // Inicio de sesión exitoso, redirigir a HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Cierra la actividad de inicio de sesión
        } else {
            // Mostrar mensaje de error si los datos no coinciden
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}
