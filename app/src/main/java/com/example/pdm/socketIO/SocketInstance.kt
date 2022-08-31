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

    fun conectar() {
        // Anuncia que el cliente esta disponible al servidor.
        cliente.on(Socket.EVENT_CONNECT) {
            cliente.emit(Eventos.CLIENTE_DISPONIBLE.id)
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
            Eventos.MENSAJE_ENVIADO_PRIVADO.id,
            mensaje
        )
    }

    fun desconectar() {
        // Anuncia al servidor que el cliente se desconecto.
        cliente.emit(Eventos.CLIENTE_TERMINADO.id)

        cliente.disconnect()
    }

    fun realizarPeticionVideoLlamadaPrivada(destinatario: String) {
        // Realiza una peticion para una video llamada privada.
        cliente.emit(Eventos.PETICION_VIDEO_LLAMADA.id, destinatario)
    }

    fun aceptarPeticionVideoLlamada(destinatario: String) {
        // Envia un evento para acepar la peticion.
        cliente.emit(Eventos.VIDEO_LLAMADA_ACEPTADA.id, destinatario)
    }

    fun rechazarPeticionVideoLlamada(destinatario: String) {
        // Envia un evento para rechazar la peticion.
        cliente.emit(Eventos.VIDEO_LLAMADA_NEGADA.id, destinatario)
    }

    fun enviarCodigoVideoLlamada(destinatario: String) {
        cliente.emit(Eventos.STREAMING_VIDEO_LLAMADA.id, destinatario)
    }
}