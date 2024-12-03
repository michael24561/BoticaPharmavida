package com.tecsup.boticaphar

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PagoTarjetaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago_tarjeta)

        val creditCardNumber = findViewById<EditText>(R.id.credit_card_number)
        val expirationDate = findViewById<EditText>(R.id.expiration_date)
        val cvvCode = findViewById<EditText>(R.id.cvv_code)
        val cardTypeIcon = findViewById<ImageView>(R.id.card_type_icon)

        // Configurar el filtro para limitar la longitud de número de tarjeta a 19 caracteres
        creditCardNumber.filters = arrayOf(InputFilter.LengthFilter(19))

        // Añadir un TextWatcher al EditText para el número de la tarjeta
        creditCardNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    val formatted = formatCardNumber(s.toString())
                    if (s.toString() != formatted) {
                        creditCardNumber.setText(formatted)
                        creditCardNumber.setSelection(formatted.length)
                    }
                    updateCardTypeIcon(s.toString(), cardTypeIcon)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Configurar el filtro para limitar la longitud y formato de la fecha de expiración
        expirationDate.filters = arrayOf(InputFilter.LengthFilter(5))
        expirationDate.inputType = InputType.TYPE_CLASS_NUMBER
        expirationDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length == 2 && !s.contains("/")) {
                    expirationDate.setText("$s/")
                    expirationDate.setSelection(expirationDate.length())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Configurar el filtro para limitar la longitud de CVV a 3 caracteres
        cvvCode.filters = arrayOf(InputFilter.LengthFilter(3))
        cvvCode.inputType = InputType.TYPE_CLASS_NUMBER
    }

    // Función para formatear el número de tarjeta con guiones
    private fun formatCardNumber(cardNumber: String): String {
        val cleanCardNumber = cardNumber.replace("[^\\d]".toRegex(), "")
        val formattedCardNumber = StringBuilder()

        for (i in cleanCardNumber.indices) {
            if (i > 0 && i % 4 == 0) {
                formattedCardNumber.append("-")
            }
            formattedCardNumber.append(cleanCardNumber[i])
        }
        return formattedCardNumber.toString().take(19)
    }

    // Función para actualizar el icono de la tarjeta según el tipo
    private fun updateCardTypeIcon(cardNumber: String, cardTypeIcon: ImageView) {
        when {
            cardNumber.startsWith("4") -> {
                // Si comienza con '4', es Visa
                cardTypeIcon.setImageResource(R.drawable.ic_visa) // Asegúrate de tener el ícono de Visa
            }
            cardNumber.startsWith("5") -> {
                // Si comienza con '5', es MasterCard
                cardTypeIcon.setImageResource(R.drawable.ic_mastercard) // Asegúrate de tener el ícono de MasterCard
            }
            cardNumber.startsWith("3") -> {
                // Si comienza con '3', es American Express
                cardTypeIcon.setImageResource(R.drawable.ic_amex) // Asegúrate de tener el ícono de American Express
            }
            else -> {
                // Si no es ninguno de los anteriores, puedes mostrar un ícono genérico o ninguno
                cardTypeIcon.setImageResource(R.drawable.ic_default_card) // Asegúrate de tener un ícono genérico
            }
        }
    }
}
