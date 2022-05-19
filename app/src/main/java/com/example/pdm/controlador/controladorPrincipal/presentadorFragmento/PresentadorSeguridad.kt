package com.example.pdm.controlador.controladorPrincipal.presentadorFragmento

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pdm.ModeloInicial
import com.example.pdm.controlador.controladorPrincipal.fragmento.Seguridad
import com.example.pdm.modelos.CambioPassword
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.PresentadorBase
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response

class PresentadorSeguridad(private val repository: Repository) : PresentadorBase() {
    val respuestaCambio: MutableLiveData<Response<RespuestaGenerica>> = MutableLiveData()

    // Cambia la contraseña del usuario.
    fun cambiarPassword(token: String, nuevaPassword: String, antiguaPassword: String) {
        viewModelScope.launch {
            val cambioPassword = CambioPassword(nuevaPassword, antiguaPassword)
            val respuestaAPI = repository.cambiarPassword(token, cambioPassword)

            respuestaCambio.value = respuestaAPI
        }
    }

    // Establece el cambio de la contraseña si no ocurrio algun error en la API.
    fun establecerCambio(ciclo: LifecycleOwner, contexto: Context, fragmento: Seguridad) {
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

            /*
            if (it.isSuccessful) {
                // Instancia del refresh.
                val intent = Intent(contexto, ModeloInicial::class.java)

                // Eliminamos la pila de activity
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                )

                // Terminamos la actividad principal, para evitar que el usuario retorna a la misma
                fragmento.activity?.finish()

                fragmento.cambiarActivity(intent)
            }
            */
        }
    }
}