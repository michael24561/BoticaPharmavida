package com.tecsup.boticaphar.models

import android.os.Parcel
import android.os.Parcelable


data class Categoria(
    val id: Int,
    val nombre: String,
    var productos: List<Producto> = listOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Producto.CREATOR) ?: listOf() // Recupera la lista de productos
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeTypedList(productos) // Escribe la lista de productos
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Categoria> = object : Parcelable.Creator<Categoria> {
            override fun createFromParcel(parcel: Parcel): Categoria {
                return Categoria(parcel)
            }

            override fun newArray(size: Int): Array<Categoria?> {
                return arrayOfNulls(size)
            }
        }
    }
}
