import com.tecsup.boticaphar.models.DetalleFacturaCliente


data class FacturaCliente(
    val cliente: String,  // El nombre del cliente o el ID
    val detalles: List<DetalleFacturaCliente>,
    val subtotal: Double,
    val igv: Double,
    val total: Double
)