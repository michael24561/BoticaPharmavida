package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tecsup.boticaphar.R

class MetodosPagoActivity : AppCompatActivity() {

    private lateinit var totalPriceText: TextView
    private lateinit var lugarRecojo: TextView
    private lateinit var direccionRecojo: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodos_pago)

        lugarRecojo = findViewById(R.id.lugarRecojo)
        direccionRecojo = findViewById(R.id.direccionRecojo)

        val fechaRecojo: TextView = findViewById(R.id.fechaRecojo)
        val textoPlazoRecojo: TextView = findViewById(R.id.textoPlazoRecojo)

        fechaRecojo.text = "${FechaUtil.obtenerFecha(1)}"
        textoPlazoRecojo.text = "Una vez que tu pedido esté listo, tendrás hasta el día ${FechaUtil.obtenerFecha(3, mostrarHora = false)} para recogerlo."

        totalPriceText = findViewById(R.id.total_price_text)

        val totalPedido = intent.getDoubleExtra("totalPedido", 0.0)
        totalPriceText.text = "Total a Pagar: S/ ${String.format("%.2f", totalPedido)}"

        findViewById<View>(R.id.menu_retroceder5).setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
            finish()
        }

        // Configurar el botón de finalizar compra
        val finalizarCompraButton = findViewById<Button>(R.id.finalizarCompra)
        finalizarCompraButton.setOnClickListener {
            val intent = Intent(this, PagoTarjetaActivity::class.java)
            intent.putExtra("totalPedido", totalPedido)  // Pasamos el total del pedido
            startActivity(intent)
        }
    }

    fun openBoticasList(view: View) {
        val intent = Intent(this, BoticasListActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val nombreBotica = data?.getStringExtra("nombreBotica")
            val direccionBotica = data?.getStringExtra("direccionBotica")

            lugarRecojo.text = nombreBotica
            direccionRecojo.text = direccionBotica
        }
    }
}
