package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Inicio2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio2)

        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            val intent = Intent(this, Inicio3Activity::class.java)
            startActivity(intent)
        }

        val skipButton = findViewById<Button>(R.id.skip_button)
        skipButton.setOnClickListener {
            val intent = Intent(this, SessionActivity::class.java)
            startActivity(intent)
        }
    }
}