package com.tecsup.boticaphar.models

import android.os.Parcel
import android.os.Parcelable

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: Int,
    val descripcion: String,
    val imagen: String,
    val fecha_vencimiento: String,  // Nuevo campo: Fecha de vencimiento del producto
    val presentacion: String        // Nuevo campo: Presentación del producto
) : Parcelable {

    // Constructor secundario que se utiliza para crear el objeto Producto a partir de un Parcel
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",            // Nombre del producto
        parcel.readDouble(),                  // Precio del producto
        parcel.readInt(),                     // Categoría del producto
        parcel.readString() ?: "",            // Descripción del producto
        parcel.readString() ?: "",            // Imagen del producto (ruta o nombre del archivo)
        parcel.readString() ?: "",            // Fecha de vencimiento (nuevo campo)
        parcel.readString() ?: ""             // Presentación del producto (nuevo campo)
    )

    // Método para escribir el objeto en un Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)                  // ID del producto
        parcel.writeString(nombre)           // Nombre del producto
        parcel.writeDouble(precio)           // Precio del producto
        parcel.writeInt(categoria)           // Categoría del producto
        parcel.writeString(descripcion)      // Descripción del producto
        parcel.writeString(imagen)           // Imagen del producto
        parcel.writeString(fecha_vencimiento)  // Guardando la fecha de vencimiento
        parcel.writeString(presentacion)      // Guardando la presentación
    }

    // Método necesario para la interfaz Parcelable
    override fun describeContents(): Int = 0

    // Companion object para la creación de objetos Producto a partir de Parcel
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Producto> = object : Parcelable.Creator<Producto> {
            override fun createFromParcel(parcel: Parcel): Producto {
                return Producto(parcel)
            }

            override fun newArray(size: Int): Array<Producto?> {
                return arrayOfNulls(size)
            }
        }
    }
}
