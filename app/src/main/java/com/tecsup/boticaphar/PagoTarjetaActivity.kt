package com.tecsup.boticaphar

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PagoTarjetaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago_tarjeta)

        // Recuperar los datos del producto seleccionado pasados desde la actividad anterior
        val nombreProducto = intent.getStringExtra("nombreProducto")
        val precio = intent.getDoubleExtra("precio", 0.0)
        val cantidad = intent.getIntExtra("cantidad", 1)

        val creditCardNumber = findViewById<EditText>(R.id.credit_card_number)
        val expirationDate = findViewById<EditText>(R.id.expiration_date)
        val cvvCode = findViewById<EditText>(R.id.cvv_code)
        val cardTypeIcon = findViewById<ImageView>(R.id.card_type_icon)
        val nombresCompletos = findViewById<EditText>(R.id.nombresCompletos)
        val btnConfirmPayment = findViewById<Button>(R.id.btn_confirm_payment)

        // Limitar la longitud del número de tarjeta
        creditCardNumber.filters = arrayOf(InputFilter.LengthFilter(19))

        creditCardNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    val formatted = formatCardNumber(s.toString())
                    if (s.toString() != formatted) {
                        creditCardNumber.setText(formatted)
                        creditCardNumber.setSelection(formatted.length) // Asegura que el cursor esté en la posición correcta
                    }
                    updateCardTypeIcon(s.toString(), cardTypeIcon)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Limitar la longitud de la fecha de expiración a 5 caracteres (MM/AA)
        expirationDate.filters = arrayOf(InputFilter.LengthFilter(5))

        expirationDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length == 2 && !s.contains("/")) {
                    expirationDate.setText(s.toString() + "/")
                    expirationDate.setSelection(s.length + 1) // Mover el cursor después del "/"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Limitar la longitud del código CVV a 3 caracteres
        cvvCode.filters = arrayOf(InputFilter.LengthFilter(3))

        // Acción del botón para validar los campos
        btnConfirmPayment.setOnClickListener {
            val missingField = validateFields(creditCardNumber, nombresCompletos, expirationDate, cvvCode)
            if (missingField != null) {
                Toast.makeText(this, "Falta llenar el campo: $missingField", Toast.LENGTH_SHORT).show()
            } else {
                // Redirige a la actividad de carga con los datos del pedido
                val cargaIntent = Intent(this, CargaActivity::class.java)
                cargaIntent.putExtra("nombreProducto", nombreProducto)  // Usar los datos obtenidos del Intent
                cargaIntent.putExtra("precio", precio)  // Usar el precio del producto
                cargaIntent.putExtra("nombreUsuario", nombresCompletos.text.toString())
                cargaIntent.putExtra("cantidad", cantidad)  // Usar la cantidad seleccionada
                startActivity(cargaIntent)
                finish()
            }
        }
    }

    /**
     * Valida que todos los campos estén llenos.
     * @return El nombre del campo faltante, o `null` si todos están completos.
     */
    private fun validateFields(
        creditCardNumber: EditText,
        nombresCompletos: EditText,
        expirationDate: EditText,
        cvvCode: EditText
    ): String? {
        return when {
            creditCardNumber.text.isNullOrBlank() -> "Número de tarjeta"
            nombresCompletos.text.isNullOrBlank() -> "Nombre del titular"
            expirationDate.text.isNullOrBlank() -> "Fecha de expiración"
            cvvCode.text.isNullOrBlank() -> "CVV"
            else -> null // Todo está completo
        }
    }

    private fun formatCardNumber(cardNumber: String): String {
        val cleanCardNumber = cardNumber.replace("[^\\d]".toRegex(), "") // Elimina todo lo que no sea número
        val formattedCardNumber = StringBuilder()

        for (i in cleanCardNumber.indices) {
            if (i > 0 && i % 4 == 0) {
                formattedCardNumber.append("-")
            }
            formattedCardNumber.append(cleanCardNumber[i])
        }
        return formattedCardNumber.toString().take(19) // Asegúrate de no exceder 19 caracteres
    }

    private fun updateCardTypeIcon(cardNumber: String, cardTypeIcon: ImageView) {
        when {
            cardNumber.startsWith("4") -> {
                cardTypeIcon.setImageResource(R.drawable.ic_visa)
            }
            cardNumber.startsWith("5") -> {
                cardTypeIcon.setImageResource(R.drawable.ic_mastercard)
            }
            cardNumber.startsWith("3") -> {
                cardTypeIcon.setImageResource(R.drawable.ic_amex)
            }
            else -> {
                cardTypeIcon.setImageResource(R.drawable.ic_default_card)
            }
        }
    }
}
