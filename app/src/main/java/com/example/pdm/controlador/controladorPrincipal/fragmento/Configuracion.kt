package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.ModeloInicial
import com.example.pdm.R
import com.example.pdm.controlador.controladorPrincipal.fragmentoFactory.ConfiguracionFactory
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorConfiguracion
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.FragmentoBase
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragmento_login.view.*

class Configuracion : FragmentoBase() {
    // Fragmentos.
    private lateinit var fragmentoConfiguracionSeguridad: Seguridad
    private lateinit var fragmentoConfiguracionInterfaz: Interfaz
    private lateinit var fragmentoConfiguracionCuenta: Cuenta

    // Inicializamos componentes del fragmento.
    private lateinit var textViewUsername: TextView
    private lateinit var textViewEstado: TextView

    // Componente del menu de configuraciones.
    private lateinit var menuConfiguraciones: NavigationView

    // Presentador del fragmento.
    private lateinit var presentador: PresentadorConfiguracion

    // Bundle de datos pasados.
    private lateinit var paqueteDatos: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Recuperamos el paquede de datos pasados.
        paqueteDatos = requireArguments()

        // Elementos del fragmento.
        val fragmento = inflater.inflate(
            R.layout.fragmento_configuracion,
            container,
            false
        )

        // Instanciamos el contexto de la app.
        val contexto = requireContext().applicationContext

        // Instanciamos el menu de configuraciones.
        menuConfiguraciones = fragmento.findViewById(R.id.navegacionConfiguracion)

        // Instancia de fragmentos de la vista.
        fragmentoConfiguracionInterfaz = Interfaz()
        fragmentoConfiguracionSeguridad = Seguridad()
        fragmentoConfiguracionCuenta = Cuenta()

        // Asignamos el paquete de datos a los fragmentos que los usaran.
        fragmentoConfiguracionInterfaz.arguments = paqueteDatos
        fragmentoConfiguracionSeguridad.arguments = paqueteDatos
        fragmentoConfiguracionCuenta.arguments = paqueteDatos

        // Instanciamos los componentes.
        textViewUsername = fragmento.findViewById(R.id.usernameTextView)
        textViewEstado = fragmento.findViewById(R.id.estadoTextView)

        // Instanciamos el repositorio de la api.
        val repo = Repository()

        // Instanciamos el factory del fragmento.
        val factoryFragment = ConfiguracionFactory(repo)

        /// Instanciamos el presentador del fragmento
        presentador = ViewModelProvider(this, factoryFragment).get(
            PresentadorConfiguracion::class.java
        )

        fragmento.scrollMainLayout.visibility = View.GONE
        fragmento.circuloProgreso.visibility = View.VISIBLE

        // Realiza la peticion al servidor.
        presentador.getUserData(paqueteDatos.getString("token")!!)

        presentador.respuestaPeticion.observe(viewLifecycleOwner, Observer { respuesta ->
            // Validamos la respuesta del servidor y la mostramos
            if (!respuesta.isSuccessful && respuesta.errorBody() != null) {
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

                // Reestablecemos las vistas a default.
                fragmento.scrollMainLayout.visibility = View.VISIBLE
                fragmento.circuloProgreso.visibility = View.GONE
            }

            if (respuesta.isSuccessful) {
                textViewUsername.text = respuesta.body()!!.usuario.nombreUsuario
                textViewEstado.text = respuesta.body()!!.preferencia.estadoPerfil
            }
        })

        // Se escucha por eventos en el menu de configuraciones.
        menuConfiguraciones.setNavigationItemSelectedListener {
            manejarInputMenu(it, contexto)
            true
        }

        return fragmento
    }

    fun manejarInputMenu(item: MenuItem, contexto: Context) {
        // Maneja el input del menu de pie.
        when(item.itemId) {
            R.id.ajustes_cuenta-> {
                cambiarFragmento(fragmentoConfiguracionCuenta)
            }

            R.id.ajustes_seguridad-> {
                cambiarFragmento(fragmentoConfiguracionSeguridad)
            }

            R.id.ajustes_interfaz-> {
                cambiarFragmento(fragmentoConfiguracionInterfaz)
            }

            // Se cierra la sesion.
            R.id.ajustes_logout-> {
                // Se inicia la vista del menu principal
                val intent = Intent(contexto, ModeloInicial::class.java)

                // Eliminamos la pila de activity
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                )

                // Terminamos la actividad principal, para evitar que el usuario retorna a la misma
                activity?.finish()

                // Se cambia el activity al inicial.
                cambiarActivity(intent)
            }
        }
    }
}