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
    val fecha_vencimiento: String,  // Nuevo campo
    val presentacion: String        // Nuevo campo
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",  // Recuperando el valor de fecha_vencimiento
        parcel.readString() ?: ""   // Recuperando el valor de presentacion
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeDouble(precio)
        parcel.writeInt(categoria)
        parcel.writeString(descripcion)
        parcel.writeString(imagen)
        parcel.writeString(fecha_vencimiento)  // Guardando el valor de fecha_vencimiento
        parcel.writeString(presentacion)      // Guardando el valor de presentacion
    }

    override fun describeContents(): Int = 0

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
