package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.boticaphar.adapters.BoticasAdapter
import com.tecsup.boticaphar.models.Botica

class BoticasListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boticas_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewBoticas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val boticasList = listOf(
            Botica("Farma Vida", "Av. Pablo Casals 292, Trujillo 13001", R.drawable.pharmavida),
            Botica("Farma Vida", "Av. América Norte 1905, Trujillo 13001", R.drawable.pharmavida),
            Botica("Farma Vida", "Av. Salvador Lara 913, Trujillo 13001", R.drawable.pharmavida),
            Botica("Farma Vida", "Cicerón, Víctor Larco Herrera 13007", R.drawable.pharmavida),
            Botica("Farma Vida", "Prol. Union 1558, Trujillo 13006", R.drawable.pharmavida)
        )

        val adapter = BoticasAdapter(boticasList, this)
        recyclerView.adapter = adapter
    }

    // Asegúrate de que al seleccionar una botica, se pasan los datos correctamente
    fun returnSelectedBotica(botica: Botica) {
        val intent = Intent().apply {
            putExtra("nombreBotica", botica.nombre)
            putExtra("direccionBotica", botica.direccion)
            putExtra("imageBotica", botica.imagenResId)
        }

        setResult(RESULT_OK, intent)
        finish()
    }
}
