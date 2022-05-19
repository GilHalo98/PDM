package com.example.pdm.util.enumerador

enum class Eventos(val id: String) {
    CONEXION("connect"),
    DESCONEXION("disconnect"),
    MENSAJE_ENVIADO("mensaje_enviado"),
    CLIENTE_TERMINADO("cliente_terminado"),
    CLIENTE_DISPONIBLE("cliente_disponible"),
    MENSAJE_ENVIADO_PRIVADO("mensaje_privado"),
}