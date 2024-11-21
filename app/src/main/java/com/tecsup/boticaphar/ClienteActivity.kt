package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ClienteActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etDni: EditText
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        etFirstName = findViewById(R.id.et_first_name)
        etLastName = findViewById(R.id.et_last_name)
        etDireccion = findViewById(R.id.et_direccion)
        etDni = findViewById(R.id.et_dni)
        btnNext = findViewById(R.id.btn_next)

        btnNext.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val direccion = etDireccion.text.toString()
            val dni = etDni.text.toString()

            // Enviar datos al siguiente layout
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("first_name", firstName)
            intent.putExtra("last_name", lastName)
            intent.putExtra("direccion", direccion)
            intent.putExtra("dni", dni)
            startActivity(intent)
        }
    }
}
