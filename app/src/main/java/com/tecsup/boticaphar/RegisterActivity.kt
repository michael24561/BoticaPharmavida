package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tecsup.boticaphar.models.UserData
import com.tecsup.boticaphar.network.RetrofitClient
import com.tecsup.boticaphar.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        btnRegister = findViewById(R.id.btn_register)

        // Datos recibidos del primer layout
        val firstName = intent.getStringExtra("first_name") ?: ""
        val lastName = intent.getStringExtra("last_name") ?: ""
        val direccion = intent.getStringExtra("direccion") ?: ""
        val dni = intent.getStringExtra("dni") ?: ""

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Validar que los campos no estén vacíos
            if (firstName.isEmpty() || lastName.isEmpty() || direccion.isEmpty() || dni.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar que las contraseñas coincidan
            if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userData = UserData(
                id = 0,  // El id será generado automáticamente por el backend
                first_name = firstName,
                last_name = lastName,
                email = email,
                user_id = 0,
                password = password,
                direccion = direccion,
                dni = dni
            )

            registrarUsuario(userData)
        }
    }

    private fun registrarUsuario(userData: UserData) {
        val apiService = RetrofitClient.getInstance().create(ApiService::class.java)
        apiService.registerUser(userData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()

                    // Aquí guardamos el ID y otros datos en SharedPreferences
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    // Guardamos el ID del usuario
                    editor.putInt("user_id", userData.id)  // Guardamos el ID
                    editor.putString("user_first_name", userData.first_name)  // Si lo necesitas
                    editor.putString("user_last_name", userData.last_name)  // Si lo necesitas
                    editor.putString("user_email", userData.email)  // Si lo necesitas
                    editor.putString("user_dni", userData.dni)  // Si lo necesitas
                    editor.apply()  // Guardamos los datos

                    // Redirigir a la siguiente actividad (HomeActivity)
                    val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Manejo del error, si el servidor devuelve un mensaje de error
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@RegisterActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
