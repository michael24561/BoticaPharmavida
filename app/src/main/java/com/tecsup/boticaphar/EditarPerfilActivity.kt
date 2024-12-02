package com.tecsup.boticaphar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tecsup.boticaphar.models.UserData
import com.tecsup.boticaphar.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var apellidosEditText: EditText
    private lateinit var dniEditText: EditText
    private lateinit var direccionEditText: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        // Inicializar los campos de entrada
        nombreEditText = findViewById(R.id.et_nombres)
        emailEditText = findViewById(R.id.et_email)
        apellidosEditText = findViewById(R.id.et_apellidos)
        dniEditText = findViewById(R.id.et_num_documento)
        direccionEditText = findViewById(R.id.et_direccion)
        btnGuardar = findViewById(R.id.btn_guardar)

        // Recuperar el ID del usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("authPrefs", Context.MODE_PRIVATE)
        val clienteId = sharedPreferences.getInt("cliente_id", -1)

        val retrocederButton = findViewById<ImageView>(R.id.menu_retroceder1)
        retrocederButton.setOnClickListener {
            onBackPressed() // Retrocede a la actividad anterior
        }

        if (clienteId != -1) {
            // Cargar los datos actuales del usuario
            Log.d("EditarPerfilActivity", "Recuperando datos del usuario con ID: $clienteId")
            getUserData(clienteId)
        } else {
            Log.e(
                "EditarPerfilActivity",
                "Error al obtener el ID del cliente desde SharedPreferences"
            )
            Toast.makeText(this, "Error al obtener los datos del usuario.", Toast.LENGTH_SHORT)
                .show()
        }

        btnGuardar.setOnClickListener {
            // Obtener los datos del formulario
            val nombre = nombreEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val apellidos = apellidosEditText.text.toString().trim()
            val dni = dniEditText.text.toString().trim()
            val direccion = direccionEditText.text.toString().trim()

            if (nombre.isEmpty() || email.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || direccion.isEmpty()) {
                Log.w("EditarPerfilActivity", "Campos incompletos al intentar guardar el perfil")
                Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val updatedUserData = UserData(
                cliente_id = clienteId,
                first_name = nombre,
                email = email,
                last_name = apellidos,
                dni = dni,
                direccion = direccion
            )

            Log.d("EditarPerfilActivity", "Datos para actualizar: $updatedUserData")
            updateUserProfile(clienteId, updatedUserData)
        }
    }

    private fun getUserData(userId: Int) {
        val apiService = RetrofitClient.getApiService()

        apiService.getUserData(userId).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    if (userData != null) {
                        Log.d("EditarPerfilActivity", "Datos del usuario recibidos: $userData")
                        nombreEditText.setText(userData.first_name)
                        emailEditText.setText(userData.email)
                        apellidosEditText.setText(userData.last_name)
                        dniEditText.setText(userData.dni)
                        direccionEditText.setText(userData.direccion)
                    } else {
                        Log.e(
                            "EditarPerfilActivity",
                            "Respuesta vacía: el cuerpo del mensaje es nulo"
                        )
                    }
                } else {
                    Log.e(
                        "EditarPerfilActivity",
                        "Error en la respuesta de la API: ${response.code()} - ${response.message()}"
                    )
                    Toast.makeText(
                        this@EditarPerfilActivity,
                        "Error al obtener los datos del usuario.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditarPerfilActivity", "Error de conexión: ${t.message}")
                Toast.makeText(
                    this@EditarPerfilActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }


    private fun updateUserProfile(userId: Int, updatedUserData: UserData) {
        val apiService = RetrofitClient.getApiService()

        apiService.updateUserProfile(userId, updatedUserData).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    Log.d("EditarPerfilActivity", "Perfil actualizado exitosamente")
                    Toast.makeText(
                        this@EditarPerfilActivity,
                        "Perfil actualizado exitosamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e(
                        "EditarPerfilActivity",
                        "Error al actualizar el perfil: ${response.code()} - ${response.message()}"
                    )
                    Toast.makeText(
                        this@EditarPerfilActivity,
                        "Error al actualizar el perfil.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("EditarPerfilActivity", "Error de conexión al actualizar: ${t.message}")
                Toast.makeText(
                    this@EditarPerfilActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}