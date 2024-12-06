package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class CargaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carga)

        // Simula un retraso para el procesamiento del pago (por ejemplo, 3 segundos)
        Handler().postDelayed({
            // Redirige a la actividad de factura con los datos del pedido
            val facturaIntent = Intent(this, MetodosPagoActivity::class.java)
            facturaIntent.putExtra("nombreProducto", intent.getStringExtra("nombreProducto"))
            facturaIntent.putExtra("precio", intent.getDoubleExtra("precio", 0.0))
            facturaIntent.putExtra("nombreUsuario", intent.getStringExtra("nombreUsuario"))
            facturaIntent.putExtra("cantidad", intent.getIntExtra("cantidad", 1))
            startActivity(facturaIntent)
            finish()
        }, 3000) // 3 segundos de retraso
    }
}
