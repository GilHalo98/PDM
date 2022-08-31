package com.example.pdm.controlador.controladorPrincipal.presentadorFragmento

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm.controlador.controladorPrincipal.adaptador.AdaptadorComponenteAdmin
import com.example.pdm.modelos.ListaUsuariosAdmin
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.PresentadorBase
import kotlinx.coroutines.launch
import retrofit2.Response

class PresentadorAdminPanel(private val repository: Repository) : PresentadorBase() {
    val respuestaListaUsuarios: MutableLiveData<Response<ListaUsuariosAdmin>> = MutableLiveData()
    val respuestaEliminarUsuario: MutableLiveData<Response<RespuestaGenerica>> = MutableLiveData()

    fun getListaUsuarios(token: String, correo: String) {
        viewModelScope.launch {
            val respuestaAPI = repository.listarUsuarios(token, correo)

            respuestaListaUsuarios.value = respuestaAPI
        }
    }

    fun eliminarUsuario(token: String, usuarioEliminar: String) {
        viewModelScope.launch {
            val respuestaAPI = repository.eliminarUsuario(token, usuarioEliminar)

            respuestaEliminarUsuario.value = respuestaAPI
        }
    }

    // Muestra los contactos del usuario de la lista del usuario en el recycleView.
    fun mostrarUsuarios(ciclo: LifecycleOwner, recycleView: RecyclerView, adaptadorRecicleView: AdaptadorComponenteAdmin) {
        // Observamos la respuesta de la peticion.
        respuestaListaUsuarios.observe(ciclo) {
            // Verificamos que sea una respuesta correcta.
            if (it.isSuccessful) {
                // Establecer la lista de contactos del adaptador a la lista de contactos pasada por
                // la peticion echa al endpoint.
                adaptadorRecicleView.establcerListaUsuarios(it.body()!!.usuarios)
                recycleView.adapter = adaptadorRecicleView
            }
        }
    }
}