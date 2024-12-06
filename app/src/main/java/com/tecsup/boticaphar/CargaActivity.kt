package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tecsup.boticaphar.models.Carrito

class CargaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carga)

        // Recupera el nombre del usuario desde SharedPreferences
        val username = getUsernameFromSharedPrefs()
        Log.d("CargaActivity", "Nombre de usuario: $username") // Verifica si el nombre de usuario es correcto

        // Simula un retraso para el procesamiento del pago (por ejemplo, 3 segundos)
        Handler().postDelayed({
            // Mostrar mensaje de pago exitoso
            Toast.makeText(this, "Pago exitoso", Toast.LENGTH_SHORT).show()

            // Vaciar el carrito después del pago exitoso
            Carrito.vaciarCarrito(this, username)
            Log.d("CargaActivity", "Carrito vaciado para el usuario: $username")

            // Redirige a la actividad principal (HomeActivity) con los datos del pedido
            val facturaIntent = Intent(this, HomeActivity::class.java)
            facturaIntent.putExtra("nombreProducto", intent.getStringExtra("nombreProducto"))
            facturaIntent.putExtra("precio", intent.getDoubleExtra("precio", 0.0))
            facturaIntent.putExtra("nombreUsuario", username) // Usa el username recuperado
            facturaIntent.putExtra("cantidad", intent.getIntExtra("cantidad", 1))

            // Inicia la actividad
            startActivity(facturaIntent)

            // Finaliza la actividad actual para no permitir que se regrese
            finish()
        }, 3000) // 3 segundos de retraso
    }

    // Método para recuperar el nombre de usuario desde SharedPreferences
    private fun getUsernameFromSharedPrefs(): String {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "usuario") ?: "usuario"
    }
}
