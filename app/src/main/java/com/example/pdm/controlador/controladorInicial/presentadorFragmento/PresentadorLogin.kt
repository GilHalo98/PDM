package com.example.pdm.controlador.controladorInicial.presentadorFragmento

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pdm.modelos.CuerpoLogin
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.PresentadorBase
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import retrofit2.Response

class PresentadorLogin(private val repository: Repository): PresentadorBase() {
    val respuestaLogin: MutableLiveData<Response<RespuestaGenerica>> = MutableLiveData()
    lateinit var token: String

    fun login(inputCorreo: TextInputLayout, inputPassword: TextInputLayout) {
        val cuerpo = CuerpoLogin(
            inputCorreo.editText!!.text.toString(),
            inputPassword.editText!!.text.toString(),
        )

        viewModelScope.launch {
            val respuestaAPI = repository.login(cuerpo)

            token = getHeader(respuestaAPI.headers(), "token")

            respuestaLogin.value = respuestaAPI
        }
    }
}