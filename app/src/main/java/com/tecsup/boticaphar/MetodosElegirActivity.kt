package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MetodosElegirActivity: AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodos_elegir)

        val menuRetroceder = findViewById<ImageView>(R.id.menu_retroceder6)
        menuRetroceder.setOnClickListener {
            val intent = Intent(this, MetodosPagoActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}