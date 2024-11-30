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
    val stock: String,
    val fecha_vencimiento: String,
    val presentacion: String,
    var cantidad: Int = 1,
    val proveedor: Int
) : Parcelable {

    // Constructor secundario para crear un objeto Producto a partir de un Parcel
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeDouble(precio)
        parcel.writeInt(categoria)
        parcel.writeString(descripcion)
        parcel.writeString(imagen)
        parcel.writeString(fecha_vencimiento)
        parcel.writeString(presentacion)
        parcel.writeInt(cantidad)
        parcel.writeInt(proveedor)
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
