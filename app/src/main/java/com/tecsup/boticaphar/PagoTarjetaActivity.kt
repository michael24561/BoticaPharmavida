package com.tecsup.boticaphar

import android.content.Context
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
import com.tecsup.boticaphar.models.Pedido
import com.tecsup.boticaphar.models.Producto
import com.tecsup.boticaphar.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            // Validación de campos
            val missingField = validateFields(creditCardNumber, expirationDate, cvvCode)
            if (missingField != null) {
                Toast.makeText(this, "Falta llenar el campo: $missingField", Toast.LENGTH_SHORT).show()
            } else {
                // Obtener el nombre del usuario
                val nombreUsuario = nombresCompletos.text.toString()

                // Obtener productos desde el Intent
                val productos = intent.getParcelableArrayListExtra<Producto>("productos") ?: emptyList()

                // Asegúrate de que totalPedido esté correctamente inicializado
                val totalPedido = intent.getDoubleExtra("totalPedido", 0.0) // O usa el cálculo que corresponda

                // Crear el objeto Pedido con la información necesaria
                val pedido = Pedido(
                    id = 0, // El ID se generará automáticamente en la base de datos
                    fecha_pedido = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()), // Fecha actual
                    total_pedido = totalPedido, // El total calculado previamente
                    estado = "Pendiente", // Estado inicial del pedido
                    cantidad = productos.size, // Cantidad de productos
                    precio_compra = totalPedido, // Precio total del pedido
                    productoId = if (productos.isNotEmpty()) productos.first().id else 0, // Aseguramos que no esté vacío
                    proveedorId = 1 // Suponiendo que el proveedor es constante, este puede venir de la API si es necesario
                )

                // Realizar la solicitud al servidor para crear el pedido
                RetrofitClient.instance.realizarPedido(pedido).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@PagoTarjetaActivity, "Pedido realizado con éxito", Toast.LENGTH_SHORT).show()

                            // Redirigir a la actividad de carga
                            val cargaIntent = Intent(this@PagoTarjetaActivity, CargaActivity::class.java)
                            cargaIntent.putExtra("nombreProducto", nombreProducto)
                            cargaIntent.putExtra("cantidad", cantidad)
                            cargaIntent.putExtra("nombreUsuario", nombreUsuario)
                            startActivity(cargaIntent)
                            finish()
                        } else {
                            // Manejo de error basado en el código de respuesta HTTP
                            val errorMessage = response.message() ?: "Error desconocido"
                            Toast.makeText(this@PagoTarjetaActivity, "Error al realizar el pedido: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Error de conexión
                        Toast.makeText(this@PagoTarjetaActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
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
