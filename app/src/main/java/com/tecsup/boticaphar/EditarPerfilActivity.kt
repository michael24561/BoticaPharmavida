package com.tecsup.boticaphar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tecsup.boticaphar.models.UserData
import com.tecsup.boticaphar.network.ApiService
import com.tecsup.boticaphar.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var apellidosEditText: EditText
    private lateinit var dniEditText: EditText
    private lateinit var direccionEditText: EditText  // Campo para la dirección
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        // Inicializar los campos de entrada
        nombreEditText = findViewById(R.id.et_nombres)
        emailEditText = findViewById(R.id.et_email)
        apellidosEditText = findViewById(R.id.et_apellidos)
        dniEditText = findViewById(R.id.et_num_documento)
        direccionEditText = findViewById(R.id.et_direccion)  // Inicialización para la dirección
        btnGuardar = findViewById(R.id.btn_guardar)

        obtenerDatosUsuario()

        btnGuardar.setOnClickListener {
            actualizarPerfil()
        }
    }

    private fun obtenerDatosUsuario() {
        val userId = getUserId() // Obtener el ID del usuario desde SharedPreferences
        if (userId != -1) {
            val apiService = RetrofitClient.getInstance().create(ApiService::class.java)

            // Obtener datos del usuario
            apiService.getUserData(userId).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    if (response.isSuccessful) {
                        response.body()?.let { userData ->
                            nombreEditText.setText(userData.first_name)
                            emailEditText.setText(userData.email)
                            apellidosEditText.setText(userData.last_name)
                            dniEditText.setText(userData.dni)
                            direccionEditText.setText(userData.direccion)  // Mostrar la dirección
                        }
                    } else {
                        Toast.makeText(this@EditarPerfilActivity, "Error al obtener datos: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Toast.makeText(this@EditarPerfilActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this, "ID de usuario no válido.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarPerfil() {
        val nombre = nombreEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val apellidos = apellidosEditText.text.toString().trim()
        val dni = dniEditText.text.toString().trim()
        val direccion = direccionEditText.text.toString().trim()

        if (nombre.isEmpty() || email.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = getUserId()
        if (userId != -1) {
            val apiService = RetrofitClient.getInstance().create(ApiService::class.java)

            val updatedUserData = UserData(
                id = userId,
                first_name = nombre,
                last_name = apellidos,
                email = email,
                dni = dni,
                direccion = direccion // Solo enviamos los campos que queremos actualizar
            )

            // Log para ver los datos antes de enviarlos
            Log.d("EditarPerfil", "Enviando datos al servidor: $updatedUserData")

            apiService.updateUserProfile(userId, updatedUserData).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditarPerfilActivity, "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Log para ver el error en la respuesta
                        Log.e("EditarPerfil", "Error en la respuesta: ${response.code()} - ${response.errorBody()?.string()}")
                        Toast.makeText(this@EditarPerfilActivity, "Error al actualizar: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    // Log para ver el error de conexión
                    Log.e("EditarPerfil", "Error de conexión: ${t.message}", t)
                    Toast.makeText(this@EditarPerfilActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this, "ID de usuario no válido.", Toast.LENGTH_SHORT).show()
        }
    }



    private fun getUserId(): Int {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", -1) // Obtener el ID del usuario almacenado en SharedPreferences
    }
}
