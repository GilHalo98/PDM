/*
    TODO:
        - Mostrar la lista de usuarios en el recycleView.

*/

package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.R
import com.example.pdm.controlador.controladorPrincipal.adaptador.AdaptadorComponenteAdmin
import com.example.pdm.controlador.controladorPrincipal.fragmentoFactory.AdminPanelFactory
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorAdminPanel
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.FragmentoBase
import kotlinx.android.synthetic.main.componente_confirmacion_password.view.*
import kotlinx.android.synthetic.main.fragmento_admin_panel.view.*
import kotlinx.android.synthetic.main.fragmento_admin_panel.view.cargaLayout
import kotlinx.android.synthetic.main.fragmento_admin_panel.view.mainLayout
import kotlinx.android.synthetic.main.fragmento_configuracion.view.*
import kotlinx.android.synthetic.main.fragmento_login.view.*
import kotlinx.android.synthetic.main.fragmento_mensajes.view.*
import kotlinx.android.synthetic.main.fragmento_mensajes.view.inputBusquedaCorreo

class AdminPanel : FragmentoBase() {
    // Precentador del fragmento.
    private lateinit var presentador: PresentadorAdminPanel

    // Constructor de dialogos.
    private lateinit var constructorDialogo: AlertDialog.Builder

    // Vistas de los dialogos.
    private lateinit var componenteConfirmacion: View

    // Dialogos.
    private lateinit var dialogoConfirmacion: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Elementos del fragmento.
        fragmento = inflater.inflate(
            R.layout.fragmento_admin_panel,
            container,
            false
        )

        // Recuperamos el paquede de datos pasados.
        paqueteDatos = requireArguments()

        // Recuperamos el token de la sesion.
        val token = paqueteDatos.get("token").toString()

        // Inicializamos los contructores de dialogos.
        constructorDialogo = AlertDialog.Builder(this.context)

        // Inicializamos las vistas de los dialogos.
        componenteConfirmacion = layoutInflater.inflate(
            R.layout.componente_confirmacion_operacion,
            null
        )

        // Establecemos las vistas a los constructores.
        constructorDialogo.setView(componenteConfirmacion)

        // Creamos los dialogos.
        dialogoConfirmacion = constructorDialogo.create()

        val repo = Repository()

        // Contexto del Fragmento.
        val contexto = requireContext().applicationContext

        // Factory del fragmento.
        val factoryFragment = AdminPanelFactory(repo)

        // Presentador del fragmento.
        presentador = ViewModelProvider(
            this,
            factoryFragment
        ).get(PresentadorAdminPanel::class.java)

        // Se instancia el adaptador del recycleView.
        val recycleView = fragmento.findViewById<RecyclerView>(R.id.componenteListaUsuarios)
        recycleView.layoutManager = LinearLayoutManager(contexto)
        val adaptadorRecicleView = AdaptadorComponenteAdmin(contexto)
        recycleView.adapter = adaptadorRecicleView

        // Muestra un layout que indica que el fragmento se esta cargando.
        fragmento.mainLayout.visibility = View.GONE
        fragmento.cargaLayout.visibility = View.VISIBLE

        // Se realiza la primera peticion de usuarios.
        presentador.getListaUsuarios(token, "")

        // Se muestran los usuarios de la primera peticion.
        presentador.mostrarUsuarios(viewLifecycleOwner, recycleView, adaptadorRecicleView)

        // Oculta el layout que indica que el fragmento se esta cargando.
        fragmento.mainLayout.visibility = View.VISIBLE
        fragmento.cargaLayout.visibility = View.GONE

        // Escucha cuando se preciona el boton de eliminar usuario en el fragmento.
        adaptadorRecicleView.onEliminarUsuario = {
            // Instanciamos el usuario que se quiere eliminar.
            val usuario = it

            // Se muestra un cuadro de dialogo para saber si la operacion se realizara.
            dialogoConfirmacion.show()

            // Si se preciona el boton de cancelar, se cierra el dialogo.
            componenteConfirmacion.botonCancelar.setOnClickListener {
                // Termina el cuadro de dialogo.
                dialogoConfirmacion.hide()
            }

            // Si se confirma la operacion, se realiza la operacion.
            componenteConfirmacion.botonConfirmar.setOnClickListener {
                // Se elimina el usuario
                presentador.eliminarUsuario(token, usuario.id.toString())

                // El listado de usuarios es actualizado
                presentador.getListaUsuarios(token,"")

                // Se muestran los usuarios de la primera peticion.
                presentador.mostrarUsuarios(viewLifecycleOwner, recycleView, adaptadorRecicleView)

                // Termina el cuadro de dialogo.
                dialogoConfirmacion.hide()
            }
        }

        fragmento.botonBusquedaAdmin.setOnClickListener {
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
            }

            // Se muestran los usuarios de la primera peticion.
            presentador.mostrarUsuarios(viewLifecycleOwner, recycleView, adaptadorRecicleView)
        }

        return fragmento
    }
}