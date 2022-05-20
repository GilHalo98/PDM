package com.example.pdm.controlador.controladorPrincipal.presentadorFragmento

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.controlador.controladorPrincipal.adaptador.AdaptadorComponenteMensajes
import com.example.pdm.modelos.AgregarContacto
import com.example.pdm.modelos.RespuestaContactosUsuario
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.PresentadorBase
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response

class PresentadorMensajes(private val repository: Repository) : PresentadorBase() {
    val respuestaPeticion: MutableLiveData<Response<RespuestaContactosUsuario>> = MutableLiveData()
    val respuestaBusqueda: MutableLiveData<Response<RespuestaContactosUsuario>> = MutableLiveData()
    val respuestaAgregarContacto: MutableLiveData<Response<RespuestaGenerica>> = MutableLiveData()

    // Retorna la respuesta de agregar un usuario a la lista de contactos.
    fun agregarContacto(token: String, contacto: String) {
        viewModelScope.launch {
            val cuerpo = AgregarContacto(contacto)
            val respuestaAPI = repository.agregarContacto(token, cuerpo)
            respuestaAgregarContacto.value = respuestaAPI
        }
    }


    // Retorna los contactos de la lista de contactos del usuario.
    fun getContactosUsuario(token: String) {
        viewModelScope.launch {
            val respuestaAPI = repository.contactosUsuario(token)

            respuestaPeticion.value = respuestaAPI
        }
    }

    // Retorna los contactos encontrados de la busqueda.
    fun getBuscarUsuarios(token: String, correo: String) {
        viewModelScope.launch {
            val respuestaAPI = repository.busquedaUsuario(token, correo)

            respuestaBusqueda.value = respuestaAPI
        }
    }

    // Muestra los contactos del usuario de la lista del usuario en el recycleView.
    fun mostrarContactos(ciclo: LifecycleOwner, recycleView: RecyclerView, adaptadorRecicleView: AdaptadorComponenteMensajes) {
        // Observamos la respuesta de la peticion.
        respuestaPeticion.observe(ciclo) {
            // Verificamos que sea una respuesta correcta.
            if (it.isSuccessful) {
                // Establecer la lista de contactos del adaptador a la lista de contactos pasada por
                // la peticion echa al endpoint.
                adaptadorRecicleView.establcerListaContactos(it.body()!!.coincidencias)
                recycleView.adapter = adaptadorRecicleView
            }
        }
    }

    // Muestra la respuesta del servidor al agregar un usuario a la lista de contactos.
    fun mostrarRespuestaAgregarContacto(ciclo: LifecycleOwner, contexto: Context) {
        respuestaAgregarContacto.observe(ciclo) {

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

        }
    }

    // Muestra las coincidencias de la busqueda en el recycleView.
    fun mostrarCoincidencias(ciclo: LifecycleOwner, recycleView: RecyclerView, adaptadorRecicleView: AdaptadorComponenteMensajes) {
        // Observamos la respuesta de la peticion.
        respuestaBusqueda.observe(ciclo) {
            // Verificamos que sea una respuesta correcta.
            if (it.isSuccessful) {
                // Establecer la lista de contactos del adaptador a la lista de contactos pasada por
                // la peticion echa al endpoint.
                adaptadorRecicleView.establcerListaContactos(it.body()!!.coincidencias)
                recycleView.adapter = adaptadorRecicleView
            }
        }
    }
}