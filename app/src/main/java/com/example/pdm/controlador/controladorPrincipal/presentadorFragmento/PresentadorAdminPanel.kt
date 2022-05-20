package com.example.pdm.controlador.controladorPrincipal.presentadorFragmento

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pdm.modelos.EliminarUsuarioAdmin
import com.example.pdm.modelos.ListaUsuariosAdmin
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.PresentadorBase
import kotlinx.coroutines.launch
import retrofit2.Response

class PresentadorAdminPanel(private val repository: Repository) : PresentadorBase() {
    val respuestaListaUsuarios: MutableLiveData<Response<ListaUsuariosAdmin>> = MutableLiveData()
    val respuestaEliminarUsuario: MutableLiveData<Response<RespuestaGenerica>> = MutableLiveData()

    fun getUserData(token: String, correo: String) {
        viewModelScope.launch {
            val respuestaAPI = repository.listarUsuarios(token, correo)

            respuestaListaUsuarios.value = respuestaAPI
        }
    }

    fun eliminarUsuario(token: String, usuarioEliminar: String) {
        val usuarioEliminado = EliminarUsuarioAdmin(usuarioEliminar)

        viewModelScope.launch {
            val respuestaAPI = repository.eliminarUsuario(token, usuarioEliminado)

            respuestaEliminarUsuario.value = respuestaAPI
        }
    }
}