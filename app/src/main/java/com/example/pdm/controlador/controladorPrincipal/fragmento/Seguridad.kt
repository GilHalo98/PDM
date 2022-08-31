package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.pdm.R
import com.example.pdm.controlador.controladorPrincipal.fragmentoFactory.SeguridadFactory
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorInterfaz
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorSeguridad
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.FragmentoBase
import com.example.pdm.util.validacionCampos
import kotlinx.android.synthetic.main.fragmento_configuracion_seguridad.view.*
import kotlinx.android.synthetic.main.fragmento_registro.view.*

class Seguridad : FragmentoBase() {
    // Inicializamos componentes del fragmento.
    private lateinit var botonCambiarPassword: Button

    // Para validar los campos.
    private val validadorCampos = validacionCampos()

    // Presentador del fragmento.
    private lateinit var presentador: PresentadorSeguridad

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Recuperamos el paquede de datos pasados.
        paqueteDatos = requireArguments()

        // Recuperamos el token de la sesion.
        val token = paqueteDatos.get("token").toString()

        // Elementos del fragmento.
        fragmento = inflater.inflate(
            R.layout.fragmento_configuracion_seguridad,
            container,
            false
        )

        // Instanciamos el contexto de la app.
        val contexto = requireContext().applicationContext

        // Instanciamos los componentes.
        botonCambiarPassword = fragmento.findViewById(R.id.botonCambiarPassword)

        // Instanciamos el repositorio de la api.
        val repo = Repository()

        // Instanciamos el factory del fragmento.
        val factoryFragment = SeguridadFactory(repo)

        // Instanciamos el presentador del fragmento
        presentador = ViewModelProvider(
            this,
            factoryFragment
        ).get(PresentadorSeguridad::class.java)

        // Escuchamos por el boton de cambio de password.
        botonCambiarPassword.setOnClickListener {
            // Validamos la contraseña nueva.
            val validacionPassword = validadorCampos.validarPassword(
                contexto,
                fragmento.inputPasswordNueva,
                fragmento.inputPasswordNuevaConfirmacion
            )

            // Si esta validada, se continua.
            if (validacionPassword) {
                val passwordNueva = fragmento.inputPasswordNueva.editText!!.text.toString()
                val passwordAntigua = fragmento.inputPasswordAntigua.editText!!.text.toString()

                presentador.cambiarPassword(token, passwordNueva, passwordAntigua)
                presentador.establecerCambio(viewLifecycleOwner, contexto, this)
            }
        }

        return fragmento
    }
}