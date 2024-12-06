package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MetodosPagoActivity : AppCompatActivity() {

    private lateinit var totalPriceText: TextView
    private lateinit var lugarRecojo: TextView
    private lateinit var direccionRecojo: TextView
    private lateinit var lugarRecojoImagen: ImageView
    private var totalPedido: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodos_pago)

        totalPriceText = findViewById(R.id.total_price_text)
        lugarRecojo = findViewById(R.id.lugarRecojo)
        direccionRecojo = findViewById(R.id.direccionRecojo)
        lugarRecojoImagen = findViewById(R.id.lugarRecojoImagen)  // Asegúrate de que tengas este ImageView en tu layout

        val finalizarCompraButton = findViewById<Button>(R.id.finalizarCompra)

        val fechaRecojo: TextView = findViewById(R.id.fechaRecojo)
        val textoPlazoRecojo: TextView = findViewById(R.id.textoPlazoRecojo)
        fechaRecojo.text = "${FechaUtil.obtenerFecha(1)}"
        textoPlazoRecojo.text = "Una vez que tu pedido esté listo, tendrás hasta el día ${FechaUtil.obtenerFecha(3, mostrarHora = false)} para recogerlo."

        totalPedido = intent.getDoubleExtra("totalPedido", 0.0)
        totalPriceText.text = "Total a Pagar: S/ ${String.format("%.2f", totalPedido)}"

        finalizarCompraButton.setOnClickListener {
            val intent = Intent(this, PagoTarjetaActivity::class.java)
            intent.putExtra("totalPedido", totalPedido)
            startActivity(intent)
        }

        // Acción para retroceder al carrito
        findViewById<View>(R.id.menu_retroceder5).setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
            finish()
        }
    }

    // Método para abrir la lista de boticas
    fun openBoticasList(view: View) {
        val intent = Intent(this, BoticasListActivity::class.java)
        startActivityForResult(intent, 1)
    }

    // Recibir los resultados de la selección de la botica
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == 1 && data != null) {
            val nombreBotica = data.getStringExtra("nombreBotica")
            val direccionBotica = data.getStringExtra("direccionBotica")
            val imagenResId = data.getIntExtra("imageBotica", 0)

            // Actualizar los campos con los datos seleccionados
            lugarRecojo.text = nombreBotica
            direccionRecojo.text = direccionBotica
            lugarRecojoImagen.setImageResource(imagenResId ?: 0)
        }
    }
}
