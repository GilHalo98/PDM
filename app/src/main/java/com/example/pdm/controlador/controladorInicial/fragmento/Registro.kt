package com.example.pdm.controlador.controladorInicial.fragmento

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.R
import com.example.pdm.controlador.controladorInicial.fragmentoFactory.RegistroFactory
import com.example.pdm.controlador.controladorInicial.presentadorFragmento.PresentadorRegistro
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.FragmentoBase
import com.example.pdm.util.validacionCampos
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragmento_registro.view.*
import kotlinx.android.synthetic.main.fragmento_registro.view.circuloProgreso
import kotlinx.android.synthetic.main.fragmento_registro.view.inputCorreo
import kotlinx.android.synthetic.main.fragmento_registro.view.inputPassword
import kotlinx.android.synthetic.main.fragmento_registro.view.scrollMainLayout

class Registro : FragmentoBase() {
    // Instanciamos los fragmentos.
    private lateinit var fragmentoMenu: Menu
    private lateinit var fragmentoValidarCorreo: ValidacionCorreo

    // Para validar los campos.
    private val validadorCampos = validacionCampos()

    // Presentador del fragmento.
    private lateinit var presentador: PresentadorRegistro

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inicializando los fragmentos.
        fragmentoMenu = Menu()
        fragmentoValidarCorreo = ValidacionCorreo()

        // Inicializamos los datos a enviar.
        paqueteDatos = Bundle()

        // Elementos del fragmento.
        fragmento = inflater.inflate(
            R.layout.fragmento_registro,
            container,
            false
        )

        // Instanciamos el contexto de la app.
        val contexto = requireContext().applicationContext

        // Instanciamos el repositorio de la api.
        val repo = Repository()

        // Instanciamos el factory del fragmento.
        val factoryFracment = RegistroFactory(repo)

        /// Instanciamos el presentador del fracmento
        presentador = ViewModelProvider(this, factoryFracment).get(PresentadorRegistro::class.java)

        fragmento.botonAceptarRegistro.setOnClickListener {
            // Validamos los datos ingresados por el usuario.
            val validacionEmail = validadorCampos.validarEmail(contexto, fragmento.inputCorreo)
            val validacionPassword = validadorCampos.validarPassword(
                contexto,
                fragmento.inputPassword,
                fragmento.inputConfirmarPassword
            )

            if (validacionEmail && validacionPassword) {
                // Mostramos el circulo de carga.
                fragmento.scrollMainLayout.visibility = View.GONE
                fragmento.circuloProgreso.visibility = View.VISIBLE

                // Una vez validados los datos de los campos, se registra el usuario.
                presentador.registrarUsuario(
                    fragmento.inputUsuario,
                    fragmento.inputCorreo,
                    fragmento.inputPassword,
                    fragmento.switchRegistroAdmin
                )

                presentador.respuestaGenerica.observe(viewLifecycleOwner, Observer { respuesta ->
                    if (!respuesta.isSuccessful && respuesta.errorBody() != null) {
                        // Reestablecemos las vistas a default.
                        fragmento.scrollMainLayout.visibility = View.VISIBLE
                        fragmento.circuloProgreso.visibility = View.GONE

                        try {
                            val respuestaError = Gson().fromJson(
                                respuesta.errorBody()!!.string(),
                                RespuestaGenerica::class.java
                            )
                            mostrarToast(
                                contexto,
                                respuestaError.codigo_respuesta.toString()
                            )

                        } catch (excepcion: Exception) {
                            Log.d("Error en la APP", excepcion.toString())
                        }

                    } else {
                        mostrarToast(
                            contexto,
                            respuesta.body()!!.codigo_respuesta.toString()
                        )
                    }

                    if (respuesta.isSuccessful) {
                        // Establecemos una variable con el correo ingresado.
                        val correoIngresado = fragmento.inputCorreo.editText!!.text.toString()

                        // Enviamos el correo al siguiente fragmento.
                        paqueteDatos.putString("correo", correoIngresado)

                        // Asignamos el paquete de datos al fragmento.
                        fragmentoValidarCorreo.arguments = paqueteDatos

                        // Cambiamos el fragmento a la validacion de correo.
                        cambiarFragmento(fragmentoValidarCorreo)
                    }
                })
            }
        }

        fragmento.botonCancelarRegistro.setOnClickListener {
            cambiarFragmento(fragmentoMenu)
        }

        return fragmento
    }
}