package com.example.pdm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdm.model.RespuestaRegistro
import com.example.pdm.model.Usuario
import com.example.pdm.repository.Repository
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOError
import java.io.IOException

class ValidarCorreoPresentador(private val repository: Repository): ViewModel() {
    val respuestaVerificacion: MutableLiveData<RespuestaRegistro> = MutableLiveData()
    val respuestaReenvio: MutableLiveData<RespuestaRegistro> = MutableLiveData()

    fun verificarCorreo(inputCodigo: TextInputLayout, correo: String) {
        viewModelScope.launch {
            val respuestaAPI = repository.validarCorreo(correo, inputCodigo.editText!!.text.toString())

            if (respuestaAPI.isSuccessful) {
                respuestaVerificacion.value = respuestaAPI.body()

            } else {
                val respuestaError = Gson().fromJson(
                    respuestaAPI.errorBody()?.string(),
                    RespuestaRegistro::class.java
                )

                respuestaVerificacion.value = respuestaError
            }
        }
    }

    fun reenviarCorreo(correo: String) {
        viewModelScope.launch {
            val respuestaAPI = repository.enviarValidacion(correo)

            if (respuestaAPI.isSuccessful) {
                respuestaReenvio.value = respuestaAPI.body()

            } else {
                val respuestaError = Gson().fromJson(
                    respuestaAPI.errorBody()?.string(),
                    RespuestaRegistro::class.java
                )

                respuestaReenvio.value = respuestaError
            }
        }
    }
}