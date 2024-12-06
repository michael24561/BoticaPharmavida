package com.tecsup.boticaphar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PagoTarjetaActivity : AppCompatActivity() {

    private var accessToken: String? = null // Variable para guardar el token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago_tarjeta)

        // Recuperar el token almacenado en SharedPreferences
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        accessToken = sharedPreferences.getString("access_token", null)

        if (accessToken.isNullOrEmpty()) {
            Toast.makeText(this, "Error: Token no encontrado. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Acción para retroceder al carrito
        findViewById<View>(R.id.menu_retroceder8).setOnClickListener {
            startActivity(Intent(this, MetodosPagoActivity::class.java))
            finish()
        }

        // Resto del código de configuración
        val nombreProducto = intent.getStringExtra("nombreProducto")
        val cantidad = intent.getIntExtra("cantidad", 1)

        val creditCardNumber = findViewById<EditText>(R.id.credit_card_number)
        val expirationDate = findViewById<EditText>(R.id.expiration_date)
        val cvvCode = findViewById<EditText>(R.id.cvv_code)
        val cardTypeIcon = findViewById<ImageView>(R.id.card_type_icon)
        val nombresCompletos = findViewById<EditText>(R.id.nombresCompletos)
        val btnConfirmPayment = findViewById<Button>(R.id.btn_confirm_payment)

        // Limitar y formatear inputs
        creditCardNumber.filters = arrayOf(InputFilter.LengthFilter(19))
        expirationDate.filters = arrayOf(InputFilter.LengthFilter(5))
        cvvCode.filters = arrayOf(InputFilter.LengthFilter(3))

        creditCardNumber.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false // Evita bucles infinitos

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                if (s != null) {
                    val formatted = formatCardNumber(s.toString())
                    if (s.toString() != formatted) {
                        creditCardNumber.setText(formatted)
                        creditCardNumber.setSelection(formatted.length)
                    }
                    updateCardTypeIcon(s.toString(), cardTypeIcon)
                }
                isFormatting = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        expirationDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length == 2 && !s.contains("/")) {
                    expirationDate.setText(s.toString() + "/")
                    expirationDate.setSelection(s.length + 1)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnConfirmPayment.setOnClickListener {
            val missingField = validateFields(creditCardNumber, expirationDate, cvvCode)
            if (missingField != null) {
                Toast.makeText(this, "Falta llenar el campo: $missingField", Toast.LENGTH_SHORT).show()
            } else {
                // Procesar el pago
                Toast.makeText(this, "Procesando el pago con token: $accessToken", Toast.LENGTH_SHORT).show()

                // Redirige a la actividad de carga
                val cargaIntent = Intent(this, CargaActivity::class.java)
                cargaIntent.putExtra("nombreProducto", nombreProducto)
                cargaIntent.putExtra("cantidad", cantidad)
                cargaIntent.putExtra("nombreUsuario", nombresCompletos.text.toString())
                startActivity(cargaIntent)
                finish()
            }
        }
    }

    // Formatea el número de tarjeta con guiones entre cada 4 dígitos
    private fun formatCardNumber(number: String): String {
        val cleanNumber = number.replace("-", "") // Elimina los guiones existentes
        return cleanNumber.chunked(4).joinToString("-") // Divide en grupos de 4 y une con guiones
    }

    // Actualiza el ícono del tipo de tarjeta basado en el número ingresado
    private fun updateCardTypeIcon(cardNumber: String, iconView: ImageView) {
        when {
            cardNumber.startsWith("4") -> iconView.setImageResource(R.drawable.ic_visa) // Ícono de Visa
            cardNumber.startsWith("5") -> iconView.setImageResource(R.drawable.ic_mastercard) // Ícono de MasterCard
            cardNumber.startsWith("3") -> iconView.setImageResource(R.drawable.ic_amex) // Ícono de Amex
            else -> iconView.setImageResource(R.drawable.ic_default_card) // Ícono genérico
        }
    }

    // Valida que los campos requeridos no estén vacíos
    private fun validateFields(
        creditCardNumber: EditText,
        expirationDate: EditText,
        cvvCode: EditText
    ): String? {
        val cardNumber = creditCardNumber.text.toString().replace("-", "") // Elimina los guiones

        return when {
            cardNumber.isEmpty() -> "Número de tarjeta"
            cardNumber.length != 16 -> "Número de tarjeta inválido"
            expirationDate.text.isNullOrEmpty() -> "Fecha de expiración"
            cvvCode.text.isNullOrEmpty() -> "CVV"
            else -> null
        }
    }
}
