package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.R
import com.example.pdm.controlador.controladorPrincipal.adaptador.AdaptadorComponenteMensajes
import com.example.pdm.controlador.controladorPrincipal.fragmentoFactory.MensajesFactory
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorMensajes
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.FragmentoBase
import com.example.pdm.util.validacionCampos
import com.google.gson.Gson
import io.socket.client.Socket
import kotlinx.android.synthetic.main.fragmento_mensajes.view.*

class Mensajes : FragmentoBase() {
    // Fragmentos.
    private lateinit  var fragmentoChat: Chat

    // Para validar los campos.
    private val validadorCampos = validacionCampos()

    // Bundle de datos pasados.
    private lateinit var paqueteDatos: Bundle

    // Interfaz de socket.
    private lateinit var socketInterfaz: Socket

    // Presentador del fragmento.
    private lateinit var presentador: PresentadorMensajes

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Recuperamos el paquede de datos pasados.
        paqueteDatos = requireArguments()

        // Recuperamos el token de la sesion.
        val token = paqueteDatos.get("token").toString()

        // Elementos del fragmento.
        val fragmento = inflater.inflate(R.layout.fragmento_mensajes, container, false)

        // Instanciamos el contexto de la app.
        val contexto = requireContext().applicationContext

        // Instanciamos el fragmento de la sala de chat.
        fragmentoChat = Chat()

        // Instanciamos el repo.
        val repo = Repository()

        // Instanciamos el factory del fragmento.
        val factoryFragment = MensajesFactory(repo)

        // Instanciamos el presentador del fragmento.
        presentador = ViewModelProvider(
            this,
            factoryFragment
        ).get(PresentadorMensajes::class.java)

        // Instanciando el adaptador del componente del recylceView.
        val recycleView = fragmento.findViewById<RecyclerView>(R.id.componenteMensajes)
        recycleView.layoutManager = LinearLayoutManager(contexto)
        val adaptadorRecicleView = AdaptadorComponenteMensajes(contexto)
        recycleView.adapter = adaptadorRecicleView

        // Realiza la peticion al servidor.
        presentador.getContactosUsuario(token)

        // Mostramos los contactos de la lista de contactos de los usuarios.
        presentador.mostrarContactos(viewLifecycleOwner, recycleView, adaptadorRecicleView)

        // Escucha por el evento en donde se precione una target del recycleView, si es
        // precionada, se manda a la sala de chat con el contacto.
        adaptadorRecicleView.onItemClick = {
            // Este listener cambiara el fragmento a la sala de chat
            // al fragmento se le pasara el id del usuario al que le pertenece la card
            // Se supone que globalmente, el socket se conecta desde el activity
            // para de esta forma tener un cliente globla del socket sobre la parte de la app
            // en la que se acredito el usuario.
            mostrarToast(contexto, it.id.toString())

            // Agregamos los argumentos al fragmento.
            paqueteDatos.putString("destino", it.id.toString())
            paqueteDatos.putString("contacto", it.nombreUsuario)

            // Se pasan los argumentos al fragmento.
            fragmentoChat.arguments = paqueteDatos

            // Cambiamos de fragmento.
            cambiarFragmento(fragmentoChat)
        }

        // Escucha por el evento para agregar contactos a la lista de contactos.
        adaptadorRecicleView.onAddContacto = {
            // Acemos la llamada a la API para agregar el nuevo contacto.
            presentador.agregarContacto(token, it.id.toString())

            // Mostramos la respuesta del API.
            presentador.mostrarRespuestaAgregarContacto(viewLifecycleOwner, contexto)
        }

        fragmento.botonBusqueda.setOnClickListener {
            // Si el textinput esta vacio, muestra los
            // contactos de la lista de contactos del usuario.
            if (fragmento.inputBusquedaCorreo.editText!!.text.isEmpty()) {
                // Si el inputdel correo esta vacio, muestra la lista de contactos del usuario.
                presentador.getContactosUsuario(token)

                // Mostramos los contactos de la lista de contactos de los usuarios.
                presentador.mostrarContactos(viewLifecycleOwner, recycleView, adaptadorRecicleView)
            } else {
                // Si el input del correo no esta vacio, realiza la busqueda por coincidencia.
                presentador.getBuscarUsuarios(
                    token,
                    fragmento.inputBusquedaCorreo.editText!!.text.toString()
                )

                // Mostramos las coincidencias encontradas.
                presentador.mostrarCoincidencias(viewLifecycleOwner, recycleView, adaptadorRecicleView)
            }
        }

        return fragmento
    }
}