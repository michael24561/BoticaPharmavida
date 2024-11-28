package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MetodosPagoActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodos_pago)

        // Configurar botón para retroceder
        val menuRetroceder = findViewById<ImageView>(R.id.menu_retroceder5)
        menuRetroceder.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
            finish()
        }
    }

    // Método para manejar clics en "Pagar con tarjeta"
    fun pagarConTarjeta(view: android.view.View) {
        val intent = Intent(this, PagoTarjetaActivity::class.java)
        startActivity(intent)
    }

    // Método para manejar clics en "Pagar con Yape"
    fun pagarConYape(view: android.view.View) {
        try {
            val amount = 10
            val phoneNumber = "902608436"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("yape://pay?amount=$amount&to=$phoneNumber")
            startActivity(intent)
        } catch (e: Exception) {
            val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.yape"))
            startActivity(playStoreIntent)
        }
    }
}

