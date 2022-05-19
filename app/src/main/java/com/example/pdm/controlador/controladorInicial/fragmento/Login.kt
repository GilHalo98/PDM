package com.example.pdm.controlador.controladorInicial.fragmento

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.ModeloPrincipal
import com.example.pdm.R
import com.example.pdm.controlador.controladorInicial.fragmentoFactory.LoginFactory
import com.example.pdm.controlador.controladorInicial.presentadorFragmento.PresentadorLogin
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.FragmentoBase
import com.example.pdm.util.validacionCampos
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragmento_login.view.*
import kotlinx.android.synthetic.main.fragmento_login.view.inputCorreo
import kotlinx.android.synthetic.main.fragmento_login.view.inputPassword

class Login : FragmentoBase() {
    // Instanciamos los fragmentos.
    private lateinit var fragmentoMenu: Menu
    private lateinit var fragmentoValidacionCorreo: ValidacionCorreo

    // Para validar los campos.
    private val validadorCampos = validacionCampos()

    // Presentador del fragmento.
    private lateinit var presentador: PresentadorLogin

    // Bundle de datos a pasar al fragmento de validacion de correo.
    private lateinit var paqueteDatos: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inicializando los fragmentos.
        fragmentoMenu = Menu()
        fragmentoValidacionCorreo = ValidacionCorreo()

        // Inicializamos los datos a enviar.
        paqueteDatos = Bundle()

        // Elementos del fragmento.
        val fragmento = inflater.inflate(R.layout.fragmento_login, container, false)

        // Instanciamos el contexto de la app.
        val contexto = requireContext().applicationContext

        // Instanciamos el repositorio de la api.
        val repo = Repository()

        // Instanciamos el factory del fragmento.
        val factoryFragment = LoginFactory(repo)

        /// Instanciamos el presentador del fragmento
        presentador = ViewModelProvider(this, factoryFragment).get(
            PresentadorLogin::class.java
        )

        fragmento.botonAceptarLogIn.setOnClickListener {
            // Primero valdamos los datos ingresados en los campos.
            val validacionEmail = validadorCampos.validarEmail(contexto, fragmento.inputCorreo)
            val validacionPassword = validadorCampos.validarLoginPassword(
                contexto,
                fragmento.inputPassword
            )

            if (validacionEmail && validacionPassword) {
                // Mostramos el circulo de carga.
                fragmento.scrollMainLayout.visibility = View.GONE
                fragmento.circuloProgreso.visibility = View.VISIBLE

                // Enviamos el request de login.
                presentador.login(fragmento.inputCorreo, fragmento.inputPassword)

                presentador.respuestaLogin.observe(viewLifecycleOwner, Observer { respuesta ->
                    // Validamos la respuesta del servidor y la mostramos
                    if (!respuesta.isSuccessful && respuesta.errorBody() != null) {
                        // Reestablecemos las vistas a default.
                        fragmento.scrollMainLayout.visibility = View.VISIBLE
                        fragmento.circuloProgreso.visibility = View.GONE

                        try {
                            // Formatea la respuesta a JSON.
                            val respuestaError = Gson().fromJson(
                                respuesta.errorBody()!!.string(),
                                RespuestaGenerica::class.java
                            )

                            // Muestra en un Toast la respuesta del servidor.
                            mostrarToast(
                                contexto,
                                respuestaError.codigo_respuesta.toString()
                            )

                            // Si el codigo interno es 3, entonces el correo no se encuentra validado.
                            if (respuestaError.codigo_respuesta == 3) {
                                // Establecemos una variable con el correo ingresado.
                                val correoIngresado = fragmento.inputCorreo.editText!!.text.toString()

                                // Enviamos el correo al siguiente fragmento.
                                paqueteDatos.putString("correo", correoIngresado)

                                // Asignamos el paquete de datos al fragmento.
                                fragmentoValidacionCorreo.arguments = paqueteDatos

                                // Se manda al fragmento de validacion de correo.
                                cambiarFragmento(fragmentoValidacionCorreo)
                            }

                        } catch (excepcion: Exception) {
                            // Si ocurre una excepcion, se muestra un log.
                            Log.d("Error en la APP", excepcion.toString())
                        }

                    } else {
                        // Si no ocurrio algun error, entonces se muestra el mensaje.
                        mostrarToast(
                            contexto,
                            respuesta.body()!!.codigo_respuesta.toString()
                        )
                    }

                    if (respuesta.isSuccessful) {
                        // Se inicia la vista del menu principal
                        val intent = Intent(contexto, ModeloPrincipal::class.java)

                        // Eliminamos la pila de activity
                        intent.addFlags(
                            Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )

                        // Pasamos el token a la siguiente vista.
                        paqueteDatos.putString(
                            "token",
                            presentador.token
                        )

                        // Agregamos los parametros al intent.
                        intent.putExtras(paqueteDatos)

                        cambiarActivityConIdioma(
                            intent,
                            valorToken(presentador.token, "idioma"),
                            valorToken(presentador.token, "pais")
                        )
                    }
                })
            }
        }

        fragmento.botonCancelarLogin.setOnClickListener {
            cambiarFragmento(fragmentoMenu)
        }

        return fragmento
    }
}