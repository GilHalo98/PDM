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

class RegistroPresentador(private val repository: Repository): ViewModel() {
    val respuestaRegistro: MutableLiveData<Response<RespuestaRegistro>> = MutableLiveData()

    fun registrarUsuario(inputUsuario: TextInputLayout, inputCorreo: TextInputLayout, inputPassword: TextInputLayout) {
        val usuario = Usuario(
            inputUsuario.editText!!.text.toString(),
            inputCorreo.editText!!.text.toString(),
            inputPassword.editText!!.text.toString()
        )

        viewModelScope.launch {
            val respuestaAPI = repository.registrarUsuario(usuario)

            respuestaRegistro.value = respuestaAPI

        }
    }
}