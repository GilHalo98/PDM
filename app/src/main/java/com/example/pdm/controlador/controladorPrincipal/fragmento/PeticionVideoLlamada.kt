package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.R
import com.example.pdm.controlador.controladorPrincipal.fragmentoFactory.PeticionVideoLlamadaFactory
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorPeticionVideoLlamada
import com.example.pdm.repository.Repository
import com.example.pdm.socketIO.SocketInstance
import com.example.pdm.util.claseBase.FragmentoBase
import com.example.pdm.util.enumerador.Eventos
import kotlinx.android.synthetic.main.fragmento_peticion_video_llamada.view.*

class PeticionVideoLlamada : FragmentoBase() {
    // Fragmentos.
    private lateinit  var fragmentoChat: Chat

    // Precentador del fragmento.
    private lateinit var presentador: PresentadorPeticionVideoLlamada

    // Componente del fragmento.
    private lateinit var textViewUsername: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Elementos del fragmento.
        fragmento = inflater.inflate(
            R.layout.fragmento_peticion_video_llamada,
            container,
            false
        )

        // Recuperamos el paquede de datos pasados.
        paqueteDatos = requireArguments()

        // Instanciamos los componentes del fragmento.
        textViewUsername = fragmento.findViewById(R.id.textViewUsername)

        // Recuperamos el token de la sesion.
        val token = paqueteDatos.get("token").toString()

        // Destino hacia el que se envian las repuestas.
        val destinatario = paqueteDatos.get("destinatario").toString()
        val contacto = paqueteDatos.get("contacto").toString()

        // Mostramos el username del contacto.
        textViewUsername.text = contacto

        val repo = Repository()

        // Contexto del Fragmento.
        val contexto = requireContext().applicationContext

        // Instanciamos el fragmento de la sala de chat.
        fragmentoChat = Chat()

        // Factory del fragmento.
        val factoryFragment = PeticionVideoLlamadaFactory(repo)

        // Presentador del fragmento.
        presentador = ViewModelProvider(
            this,
            factoryFragment
        ).get(PresentadorPeticionVideoLlamada::class.java)

        // Se envia la aceptacion de la peticion.
        fragmento.botonAceptarPeticion.setOnClickListener {
            SocketInstance.aceptarPeticionVideoLlamada(destinatario)
        }

        // Se envia el rechazo de la peticion.
        fragmento.botonRechazarPeticion.setOnClickListener {
            SocketInstance.rechazarPeticionVideoLlamada(destinatario)

            // Agregamos los argumentos al fragmento.
            paqueteDatos.putString("destino", destinatario)
            paqueteDatos.putString("contacto", contacto)

            // Se pasan los argumentos al fragmento.
            fragmentoChat.arguments = paqueteDatos

            // Cambiamos de fragmento.
            cambiarFragmento(fragmentoChat)
        }

        return fragmento
    }
}