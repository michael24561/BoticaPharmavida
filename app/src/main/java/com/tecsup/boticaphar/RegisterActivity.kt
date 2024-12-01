package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etDni: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar los campos del formulario
        etFirstName = findViewById(R.id.et_first_name)
        etLastName = findViewById(R.id.et_last_name)
        etDireccion = findViewById(R.id.et_direccion)
        etDni = findViewById(R.id.et_dni)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        btnRegister = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            val dni = etDni.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Validaciones
            if (firstName.isEmpty() || lastName.isEmpty() || direccion.isEmpty() || dni.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear el objeto UserData
            val userData = UserData(
                cliente_id = 0,  // El ID será generado automáticamente por el backend
                first_name = firstName,
                last_name = lastName,
                email = email,
                password = password,
                direccion = direccion,
                dni = dni
            )

            // Realizar la solicitud de registro
            registrarUsuario(userData)
        }
    }

    private fun registrarUsuario(userData: UserData) {
        val apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        apiService.registerUser(userData).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    val registeredUser = response.body()
                    Log.d("RegisterActivity", "Registered User: $registeredUser")  // Loguea el usuario registrado

                    // Si el registro fue exitoso, redirige al login sin guardar nada en SharedPreferences
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()

                    // Redirigir a la actividad de login
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    Log.d("RegisterActivity", "Error: $errorMessage")
                    Toast.makeText(this@RegisterActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

