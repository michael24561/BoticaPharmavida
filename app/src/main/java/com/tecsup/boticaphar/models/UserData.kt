package com.tecsup.boticaphar.models

data class UserData(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String? = null,
    val direccion: String,
    val dni: String
)


