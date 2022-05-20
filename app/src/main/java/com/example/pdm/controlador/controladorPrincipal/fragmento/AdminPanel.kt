package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.R
import com.example.pdm.controlador.controladorPrincipal.adaptador.AdaptadorComponenteAdmin
import com.example.pdm.controlador.controladorPrincipal.adaptador.AdaptadorComponenteMensajes
import com.example.pdm.controlador.controladorPrincipal.fragmentoFactory.AdminPanelFactory
import com.example.pdm.controlador.controladorPrincipal.fragmentoFactory.MensajesFactory
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorAdminPanel
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorMensajes
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.FragmentoBase
import kotlinx.android.synthetic.main.fragmento_mensajes.view.*

class AdminPanel : FragmentoBase() {

    private lateinit var presentador: PresentadorAdminPanel

    private lateinit var paqueteDatos: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Elementos del fragmento.
        val fragmento = inflater.inflate(R.layout.fragmento_admin_panel, container, false)

        // Recuperamos el paquede de datos pasados.
        paqueteDatos = requireArguments()

        // Recuperamos el token de la sesion.
        val token = paqueteDatos.get("token").toString()

        val repo = Repository()

        val contexto = requireContext().applicationContext

        val factoryFragment = AdminPanelFactory(repo)

        val recycleView = fragmento.findViewById<RecyclerView>(R.id.componenteListaUsuarios)
        recycleView.layoutManager = LinearLayoutManager(contexto)
        val adaptadorRecicleView = AdaptadorComponenteAdmin(contexto)
        recycleView.adapter = adaptadorRecicleView

        presentador = ViewModelProvider(
            this,
            factoryFragment
        ).get(PresentadorAdminPanel::class.java)



        adaptadorRecicleView.onEliminarUsuario =
            {
                presentador.eliminarUsuario(token, it.id.toString())
                presentador.getListaUsuarios(token,"")
            }

        fragmento.botonBusqueda.setOnClickListener {
            // Si el textinput esta vacio, muestra los
            // contactos de la lista de contactos del usuario.
            if (fragmento.inputBusquedaCorreo.editText!!.text.isEmpty()) {
                // Si el inputdel correo esta vacio, muestra la lista de contactos del usuario.
                presentador.getListaUsuarios(token, "")

                // Mostramos los contactos de la lista de contactos de los usuarios.
            } else {
                // Si el input del correo no esta vacio, realiza la busqueda por coincidencia.
                presentador.getListaUsuarios(
                    token,
                    fragmento.inputBusquedaCorreo.editText!!.text.toString()
                )

                // Mostramos las coincidencias encontradas.
                //presentador.mostrarCoincidencias(viewLifecycleOwner, recycleView, adaptadorRecicleView)
            }
        }


        return fragmento
    }
}