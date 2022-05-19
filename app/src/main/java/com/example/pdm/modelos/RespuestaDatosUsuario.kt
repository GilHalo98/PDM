package com.example.pdm.modelos

data class RespuestaDatosUsuario(
    val codigo_respuesta: Int,
    val preferencia: Preferencia,
    val usuario: Usuario
)