package com.example.pdm.socketIO

import com.example.pdm.util.Constants
import com.example.pdm.util.enumerador.Eventos
import io.socket.client.IO
import io.socket.client.Socket

object SocketInstance {
    lateinit var cliente: Socket

    fun crearInstancia(token: String) {
        val opciones = IO.Options()

        val auth: Map<String, String> = mapOf("token" to token)

        opciones.auth = auth

        cliente = IO.socket(Constants.SOCKET_URI, opciones)
    }

    fun conectar(token: String) {
        // Anuncia que el cliente esta disponible al servidor.
        cliente.on(Socket.EVENT_CONNECT) {
            cliente.emit(
                Eventos.CLIENTE_DISPONIBLE.id,
                token
            )
        }

        cliente.connect()
    }

    fun enviarMensajePrivado(mensaje: String, destino: String) {
        cliente.emit(
            Eventos.MENSAJE_ENVIADO_PRIVADO.id,
            mensaje,
            destino
        )
    }

    fun enviarMensaje(mensaje: String) {
        cliente.emit(
            Eventos.MENSAJE_ENVIADO.id,
            mensaje
        )
    }

    fun desconectar(token: String) {
        // Anuncia al servidor que el cliente se desconecto.
        cliente.emit(
            Eventos.CLIENTE_TERMINADO.id,
            token
        )

        cliente.disconnect()
    }
}