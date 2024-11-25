package com.tecsup.boticaphar

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PagoTarjetaActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago_tarjeta)

        val creditCardNumber = findViewById<EditText>(R.id.credit_card_number)
        val cardTypeIcon = findViewById<ImageView>(R.id.card_type_icon)

        // Añadir un TextWatcher al EditText para detectar el número de la tarjeta
        creditCardNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Llamar a la función para detectar el tipo de tarjeta
                val cardType = detectCardType(s.toString())
                updateCardIcon(cardType, cardTypeIcon)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    // Función para detectar el tipo de tarjeta
    private fun detectCardType(cardNumber: String): String {
        return when {
            cardNumber.startsWith("4") -> "visa"
            cardNumber.startsWith("5") -> "mastercard"
            cardNumber.startsWith("3") -> "amex"
            else -> "default" // Si no es un tipo conocido, se muestra un ícono genérico
        }
    }

    // Función para actualizar el ícono según el tipo de tarjeta
    private fun updateCardIcon(cardType: String, cardTypeIcon: ImageView) {
        when (cardType) {
            "visa" -> cardTypeIcon.setImageResource(R.drawable.ic_visa) // Asegúrate de tener el ícono en los recursos
            "mastercard" -> cardTypeIcon.setImageResource(R.drawable.ic_mastercard)
            "amex" -> cardTypeIcon.setImageResource(R.drawable.ic_amex)
            else -> cardTypeIcon.setImageResource(R.drawable.ic_default_card)
        }
    }
}
