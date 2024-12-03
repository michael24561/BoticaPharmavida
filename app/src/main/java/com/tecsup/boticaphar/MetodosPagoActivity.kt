package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tecsup.boticaphar.R

class MetodosPagoActivity : AppCompatActivity() {

    private lateinit var lugarRecojo: TextView
    private lateinit var direccionRecojo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodos_pago)

        lugarRecojo = findViewById(R.id.lugarRecojo)
        direccionRecojo = findViewById(R.id.direccionRecojo)

        findViewById<View>(R.id.menu_retroceder5).setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
            finish()
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
