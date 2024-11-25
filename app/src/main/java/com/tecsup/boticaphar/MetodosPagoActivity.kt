package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MetodosPagoActivity: AppCompatActivity(){

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodos_pago)


        // Configurar botón para retroceder
        val menuRetroceder = findViewById<ImageView>(R.id.menu_retroceder5)
        menuRetroceder.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
            finish()
        }

        //Yape
        val btnPagoYape = findViewById<Button>(R.id.btn_pagar_yape)
        btnPagoYape.setOnClickListener {
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

        val btnPagoTarjeta = findViewById<Button>(R.id.btn_pagar_tarjeta)
        btnPagoTarjeta.setOnClickListener {
            val intent = Intent(this, PagoTarjetaActivity::class.java)
            startActivity(intent)
        }


    }
}