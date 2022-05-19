package com.example.pdm.modelos

data class RespuestaContactosUsuario(
    val codigo_respuesta: Int,
    val coincidencias: List<Contacto>
)