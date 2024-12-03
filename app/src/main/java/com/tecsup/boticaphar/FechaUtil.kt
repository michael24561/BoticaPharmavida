import java.text.SimpleDateFormat
import java.util.*

object FechaUtil {
    // Función que recibe un parámetro `dias` para calcular la fecha
    fun obtenerFecha(dias: Int, mostrarHora: Boolean = true): String {
        val calendario = Calendar.getInstance()
        calendario.add(Calendar.DAY_OF_YEAR, dias) // Añadimos el número de días especificados

        val formatoFecha = if (mostrarHora) {
            // Formato con hora
            SimpleDateFormat("EEEE, dd MMMM yyyy 'a las' hh:mm a", Locale("es", "ES"))
        } else {
            // Formato sin hora
            SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("es", "ES"))
        }

        return formatoFecha.format(calendario.time)
    }
}
