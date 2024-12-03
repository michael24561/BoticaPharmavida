package com.tecsup.boticaphar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.boticaphar.adapters.BoticasAdapter
import com.tecsup.boticaphar.models.Botica

class BoticasListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BoticasAdapter
    private lateinit var boticasList: List<Botica>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boticas_list)

        recyclerView = findViewById(R.id.recyclerViewBoticas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        boticasList = listOf(
            Botica("Farma Vida", "Av. Pablo Casals 292, Trujillo 13001"),
            Botica("Farma Vida", "Av. América Norte 1905, Trujillo 13001"),
            Botica("Farma Vida", "Av. Salvador Lara 913, Trujillo 13001"),
            Botica("Farma Vida", "Cicerón, Víctor Larco Herrera 13007"),
            Botica("Farma Vida", "Prol. Union 1558, Trujillo 13006")
        )

        adapter = BoticasAdapter(boticasList, this)
        recyclerView.adapter = adapter
    }
}
