package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class Inicio1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio1)

        val startButton = findViewById<Button>(R.id.start_button)
        val skipButton = findViewById<Button>(R.id.skip_button)

        startButton.setOnClickListener {
            val intent = Intent(this@Inicio1Activity, Inicio2Activity::class.java)
            startActivity(intent)
        }

        skipButton.setOnClickListener {
            val intent = Intent(this@Inicio1Activity, SessionActivity::class.java)
            startActivity(intent)
        }
    }
}