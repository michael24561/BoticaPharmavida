package com.tecsup.boticaphar

import android.content.Context
import android.os.Bundle
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
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        // Inicializar los EditText y el botón
        nombreEditText = findViewById(R.id.et_nombres)
        emailEditText = findViewById(R.id.et_email)
        apellidosEditText = findViewById(R.id.et_apellidos)
        dniEditText = findViewById(R.id.et_num_documento)
        btnGuardar = findViewById(R.id.btn_guardar)

        obtenerDatosUsuario()

        btnGuardar.setOnClickListener {
            actualizarPerfil()
        }
    }

    private fun obtenerDatosUsuario() {
        val userId = getUserId()
        if (userId != -1) {
            val apiService = RetrofitClient.getInstance().create(ApiService::class.java)
            apiService.getUserData(userId).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    if (response.isSuccessful) {
                        response.body()?.let { userData ->
                            nombreEditText.setText(userData.first_name)
                            emailEditText.setText(userData.email)
                            apellidosEditText.setText(userData.last_name)
                            dniEditText.setText(userData.dni)
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

        if (nombre.isEmpty() || email.isEmpty() || apellidos.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = getUserId()
        if (userId != -1) {
            val apiService = RetrofitClient.getInstance().create(ApiService::class.java)

            // Crear el objeto actualizado
            val updatedUserData = UserData(
                id = userId,
                first_name = nombre,
                last_name = apellidos,
                email = email,
                user_id = userId, // Mantener este valor si es requerido
                dni = dni,
                direccion = "",
                password = "" // Ajusta este campo según lo que requiera el servidor
            )

            apiService.updateCliente(userId, updatedUserData).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditarPerfilActivity, "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@EditarPerfilActivity, "Error al actualizar: ${response.code()}", Toast.LENGTH_SHORT).show()
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

    private fun getUserId(): Int {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", -1)
    }
}
