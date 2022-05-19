package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.R
import com.example.pdm.controlador.controladorPrincipal.fragmentoFactory.CuentaFactory
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorCuenta
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.FragmentoBase
import com.example.pdm.util.validacionCampos
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.componente_confirmacion_password.view.*
import kotlin.contracts.contract

class Cuenta : FragmentoBase() {
    // Inicializamos componentes del fragmento.
    private lateinit var botonCambioEstado: ImageButton
    private lateinit var botonCambioEmail: ImageButton
    private lateinit var botonCambioUsername: ImageButton

    private lateinit var inputCambioEstado: TextInputLayout
    private lateinit var inputCambioEmail: TextInputLayout
    private lateinit var inputCambioUsername: TextInputLayout

    // Constructor de dialogos.
    private lateinit var constructorDialogoUsername: AlertDialog.Builder
    private lateinit var constructorDialogoEmail: AlertDialog.Builder

    // Vistas de los dialogos.
    private lateinit var componenteConfirmacionUsername: View
    private lateinit var componenteConfirmacionEmail: View

    // Dialogos.
    private lateinit var dialogoUsername: AlertDialog
    private lateinit var dialogoEmail: AlertDialog

    // Para validar los campos.
    private val validadorCampos = validacionCampos()

    // Presentador del fragmento.
    private lateinit var presentador: PresentadorCuenta

    // Bundle de datos pasados.
    private lateinit var paqueteDatos: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Recuperamos el paquede de datos pasados.
        paqueteDatos = requireArguments()

        // Recuperamos el token de la sesion.
        val token = paqueteDatos.get("token").toString()

        // Elementos del fragmento.
        val fragmento = inflater.inflate(
            R.layout.fragmento_configuracion_cuenta,
            container,
            false
        )

        // Instanciamos el contexto de la app.
        val contexto = requireContext().applicationContext

        // Instanciamos los componentes.
        botonCambioEstado = fragmento.findViewById(R.id.botonCambioEstado)
        botonCambioEmail = fragmento.findViewById(R.id.botonCambioEmail)
        botonCambioUsername = fragmento.findViewById(R.id.botonCambioUsername)

        inputCambioEstado = fragmento.findViewById(R.id.inputCambioEstado)
        inputCambioEmail = fragmento.findViewById(R.id.inputCambioEmail)
        inputCambioUsername = fragmento.findViewById(R.id.inputCambioUsername)

        // Inicializamos los contructores de dialogos.
        constructorDialogoUsername = AlertDialog.Builder(this.context)
        constructorDialogoEmail = AlertDialog.Builder(this.context)

        // Inicializamos las vistas de los dialogos.
        componenteConfirmacionUsername = layoutInflater.inflate(
            R.layout.componente_confirmacion_password,
            null
        )
        componenteConfirmacionEmail = layoutInflater.inflate(
            R.layout.componente_confirmacion_password,
            null
        )

        // Establecemos las vistas a los constructores.
        constructorDialogoUsername.setView(componenteConfirmacionUsername)
        constructorDialogoEmail.setView(componenteConfirmacionEmail)

        // Creamos los dialogos.
        dialogoUsername = constructorDialogoUsername.create()
        dialogoEmail = constructorDialogoEmail.create()

        // Instanciamos el repositorio de la api.
        val repo = Repository()

        // Instanciamos el factory del fragmento.
        val factoryFragment = CuentaFactory(repo)

        // Instanciamos el presentador del fragmento
        presentador = ViewModelProvider(
            this,
            factoryFragment
        ).get(PresentadorCuenta::class.java)

        // Escuchamos por eventos en los botones de los componentes de los dialogos.
        componenteConfirmacionUsername.botonConfirmar.setOnClickListener {
            val validacionPassword = validadorCampos.validarLoginPassword(contexto, componenteConfirmacionUsername.inputPassword)

            // Si la contraseña de confirmacion es valida.
            if (validacionPassword) {
                val password = componenteConfirmacionUsername.inputPassword.editText!!.text.toString()
                val nuevoUsername = inputCambioUsername.editText!!.text.toString()

                // Realizamos la peticion al API.
                presentador.cambiarUsername(token, nuevoUsername, password)

                // Mostramos la respuesta de la API.
                presentador.establecerCambioUsername(viewLifecycleOwner, contexto, this)
            }
        }

        componenteConfirmacionEmail.botonConfirmar.setOnClickListener {
            val validacionPassword = validadorCampos.validarLoginPassword(contexto, componenteConfirmacionEmail.inputPassword)

            // Si la contraseña de confirmacion es valida.
            if (validacionPassword) {
                val password = componenteConfirmacionEmail.inputPassword.editText!!.text.toString()
                val nuevoUsername = inputCambioEmail.editText!!.text.toString()

                // Realizamos la peticion al API.
                presentador.cambiarEmail(token, nuevoUsername, password)

                // Mostramos la respuesta de la API.
                presentador.establecerCambioEmail(viewLifecycleOwner, contexto, this)
            }
        }

        // Escuchamos por los eventos de los botones del fragmento.
        botonCambioEstado.setOnClickListener {
            presentador.cambiarEstado(token, inputCambioEstado.editText!!.text.toString())

            // Mostramos si el cambio se realizo o no.
            presentador.establecerCambioEstado(viewLifecycleOwner, contexto, this)
        }

        botonCambioEmail.setOnClickListener{
            dialogoEmail.show()
        }

        botonCambioUsername.setOnClickListener {
            dialogoUsername.show()
        }

        return fragmento
    }
}