package com.example.pdm.controlador.controladorPrincipal.presentadorFragmento

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pdm.modelos.RespuestaDatosUsuario
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.PresentadorBase
import kotlinx.coroutines.launch
import retrofit2.Response

class PresentadorConfiguracion(private val repository: Repository) : PresentadorBase() {
    val respuestaPeticion: MutableLiveData<Response<RespuestaDatosUsuario>> = MutableLiveData()

    fun getUserData(token: String) {
        viewModelScope.launch {
            val respuestaAPI = repository.datosUsuario(token)

            respuestaPeticion.value = respuestaAPI
        }
    }
}