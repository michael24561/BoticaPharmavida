package com.tecsup.boticaphar.models

data class TokenResponse(
    val access: String,
    val refresh: String,
    val cliente_id: Int
)
