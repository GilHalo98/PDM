package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.R
import com.example.pdm.controlador.controladorPrincipal.adaptador.AdaptadorComponenteChat
import com.example.pdm.socketIO.SocketInstance
import com.example.pdm.util.claseBase.FragmentoBase
import com.example.pdm.util.enumerador.Eventos
import kotlinx.android.synthetic.main.fragmento_chat.view.*

class Chat : FragmentoBase() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Recuperamos el paquete de datos pasados.
        paqueteDatos = requireArguments()

        // Elementos del fragmento.
        fragmento = inflater.inflate(
            R.layout.fragmento_chat,
            container,
            false
        )

        // Contexto del fragmento.
        val contexto = requireContext().applicationContext

        // Inicializamos el componente del recycleView.
        val recycleView = fragmento.findViewById<RecyclerView>(R.id.componenteChat)

        // Establecemos un layout al recycleView.
        recycleView.layoutManager = LinearLayoutManager(contexto)

        // Inicializamos el adaptador del recycleView.
        val adaptadorRecycleView = AdaptadorComponenteChat(contexto)

        // Establecemos el adaptador en el componente.
        recycleView.adapter = adaptadorRecycleView

        // Recuperamos el id del usuairo en el que se conecto la sala de chat.
        fragmento.textViewUsername.text = paqueteDatos.getString("contacto")

        // Escuchamos desde la interfaz del socket si llega algun mensaje.
        SocketInstance.cliente.on(Eventos.MENSAJE_ENVIADO_PRIVADO.id) {
            activity?.runOnUiThread {
                adaptadorRecycleView.agregarMensajeRecibido(it[0].toString())
                recycleView.adapter = adaptadorRecycleView
            }
        }

        // Escuchamos si el boton de enviar es precionado.
        fragmento.botonEnviarMensaje.setOnClickListener {
            SocketInstance.enviarMensajePrivado(
                fragmento.inputMensaje.editText!!.text.toString(),
                paqueteDatos.getString("destino")!!
            )

            activity?.runOnUiThread {
                adaptadorRecycleView.agregarMensajeEnviado(
                    fragmento.inputMensaje.editText!!.text.toString()
                )
                recycleView.adapter = adaptadorRecycleView
            }

            fragmento.inputMensaje.editText!!.text.clear()
        }

        // Escuchamos por el boton de realizar video llamada.
        fragmento.botonVideoLlamadaPrivada.setOnClickListener {
            // Realiza una peticion a al contacto para una video llamada privada.
            SocketInstance.realizarPeticionVideoLlamadaPrivada(
                paqueteDatos.getString("destino")!!
            )
        }

        return fragmento
    }
}