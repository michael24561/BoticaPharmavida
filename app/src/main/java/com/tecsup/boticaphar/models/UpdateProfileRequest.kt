package com.tecsup.boticaphar.models

data class UpdateProfileRequest(
    val userId: Int,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val numDocumento: String
)
