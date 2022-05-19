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
import com.example.pdm.controlador.controladorInicial.fragmentoFactory.ValidacionCorreoFactory
import com.example.pdm.controlador.controladorInicial.presentadorFragmento.PresentadorValidacionCorreo
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.FragmentoBase
import com.example.pdm.util.validacionCampos
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragmento_validar_correo.view.*
import kotlinx.android.synthetic.main.fragmento_validar_correo.view.circuloProgreso
import kotlinx.android.synthetic.main.fragmento_validar_correo.view.scrollMainLayout

class ValidacionCorreo : FragmentoBase() {
    // Instanciamos los fragmentos.

    // Para validar los campos.
    private val validadorCampos = validacionCampos()

    // Presentador del fragmento.
    private lateinit var presentador: PresentadorValidacionCorreo

    // Bundle de datos pasados.
    private lateinit var paqueteDatos: Bundle
    private lateinit var nuevoPaqueteDatos: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Recuperamos el paquede de datos pasados.
        paqueteDatos = requireArguments()

        // Inicializamos los datos a enviar.
        nuevoPaqueteDatos = Bundle()

        // Desenpaquetamos los datos que llegaron al fragmento.
        val correo = paqueteDatos.getString("correo")!!

        // Elementos del fragmento.
        val fragmento = inflater.inflate(
            R.layout.fragmento_validar_correo,
            container,
            false
        )

        // Instanciamos el contexto de la app.
        val contexto = requireContext().applicationContext

        // Instanciamos el repositorio de la api.
        val repo = Repository()

        // Instanciamos el factory del fragmento.
        val factoryFragment = ValidacionCorreoFactory(repo)

        /// Instanciamos el presentador del fracmento
        presentador = ViewModelProvider(this, factoryFragment).get(
            PresentadorValidacionCorreo::class.java
        )

        fragmento.botonValidar.setOnClickListener {
            // Primero validamos los datos ingresados por el usuario.
            val validacionCodigo = validadorCampos.validarCodigoVerificacion(
                contexto,
                fragmento.inputCodigo
            )

            if (validacionCodigo) {
                // Mostramos el circulo de carga.
                fragmento.scrollMainLayout.visibility = View.GONE
                fragmento.circuloProgreso.visibility = View.VISIBLE

                // Si se validan, se manda la validacion del correo a la API.
                presentador.verificarCorreo(fragmento.inputCodigo, correo)

                presentador.respuestaVerificacion.observe(viewLifecycleOwner, Observer { respuesta ->
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
                        // Se inicia la vista del menu principal
                        val intent = Intent(contexto, ModeloPrincipal::class.java)

                        // Inicializamos los datos a enviar.
                        nuevoPaqueteDatos = Bundle()

                        // Eliminamos la pila de activity
                        intent.addFlags(
                            Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )

                        // Pasamos el token a la siguiente vista.
                        nuevoPaqueteDatos.putString(
                            "token",
                            presentador.token
                        )

                        // Agregamos los parametros al intent.
                        intent.putExtras(nuevoPaqueteDatos)

                        cambiarActivityConIdioma(
                            intent,
                            valorToken(presentador.token, "idioma"),
                            valorToken(presentador.token, "pais")
                        )
                    }
                })
            }
        }

        fragmento.botonReenviar.setOnClickListener {
            // Se reenvia el correo de validacion, con otro codigo distinto.
            presentador.reenviarCorreo(correo)

            // Se verifia que el correo fue reenviado.
            presentador.respuestaReenvio.observe(viewLifecycleOwner, Observer { respuesta ->
                if (!respuesta.isSuccessful && respuesta.errorBody() != null) {
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
            })
        }

        return fragmento
    }
}