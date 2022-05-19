package com.example.pdm.controlador.controladorInicial.presentadorFragmento

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.PresentadorBase
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import retrofit2.Response

class PresentadorValidacionCorreo(private val repository: Repository): PresentadorBase() {
    val respuestaVerificacion: MutableLiveData<Response<RespuestaGenerica>> = MutableLiveData()
    val respuestaReenvio: MutableLiveData<Response<RespuestaGenerica>> = MutableLiveData()
    lateinit var token: String

    fun verificarCorreo(inputCodigo: TextInputLayout, correo: String) {
        viewModelScope.launch {
            val respuestaAPI = repository.validarCorreo(
                correo,
                inputCodigo.editText!!.text.toString()
            )

            token = getHeader(respuestaAPI.headers(), "token")

            respuestaVerificacion.value = respuestaAPI
        }
    }

    fun reenviarCorreo(correo: String) {
        viewModelScope.launch {
            val respuestaAPI = repository.enviarValidacion(correo)

            respuestaReenvio.value = respuestaAPI
        }
    }
}