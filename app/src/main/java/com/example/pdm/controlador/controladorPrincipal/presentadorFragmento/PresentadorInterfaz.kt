package com.example.pdm.controlador.controladorPrincipal.presentadorFragmento

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pdm.ModeloPrincipal
import com.example.pdm.controlador.controladorPrincipal.fragmento.Interfaz
import com.example.pdm.modelos.CambioIdioma
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.PresentadorBase
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response

class PresentadorInterfaz(private val repository: Repository) : PresentadorBase() {
    val respuestaCambio: MutableLiveData<Response<RespuestaGenerica>> = MutableLiveData()
    lateinit var nuevoToken: String

    // Cambia el idioma de la interfaz.
    fun cambiarIdiomaInterfaz(token: String, nuevoIdioma: Int) {
        viewModelScope.launch {
            val cuerpo = when(nuevoIdioma) {
                0 -> {
                    CambioIdioma("es", "mx")
                }

                1 -> {
                    CambioIdioma("en", "usa")
                }

                else -> {
                    CambioIdioma("INVALID", "INVALID")
                }
            }
            val respuestaAPI = repository.cambiarIdioma(token, cuerpo)
            nuevoToken = getHeader(respuestaAPI.headers(), "token")
            respuestaCambio.value = respuestaAPI
        }
    }

    // Establece el cambio del idioma en la interfaz si no ocurrio algun error en la API.
    fun establecerCambio(ciclo: LifecycleOwner, contexto: Context, fragmento: Interfaz) {
        respuestaCambio.observe(ciclo) {

            if (!it.isSuccessful && it.errorBody() != null) {
                try {
                    // Formatea la respuesta a JSON.
                    val respuestaError = Gson().fromJson(
                        it.errorBody()!!.string(),
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
                    it.body()!!.codigo_respuesta.toString()
                )
            }

            if (it.isSuccessful) {
                // Instancia del refresh.
                val intent = Intent(contexto, ModeloPrincipal::class.java)

                // Instancia del paquete de datos.
                val paqueteDatos = Bundle()

                // Eliminamos la pila de activity
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                )

                // Terminamos la actividad principal, para evitar que el usuario retorna a la misma
                fragmento.activity?.finish()

                // Pasamos el token a la siguiente vista.
                paqueteDatos.putString(
                    "token",
                    nuevoToken
                )

                // Agregamos los parametros al intent.
                intent.putExtras(paqueteDatos)

                fragmento.cambiarIdioma(
                    intent,
                    this.valorToken(nuevoToken, "idioma"),
                    this.valorToken(nuevoToken, "pais")
                )
            }
        }
    }
}