package com.example.pdm.controlador.controladorInicial.presentadorFragmento

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pdm.modelos.CuerpoRegistro
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.PresentadorBase
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import retrofit2.Response

class PresentadorRegistro(private val repository: Repository): PresentadorBase() {
    val respuestaGenerica: MutableLiveData<Response<RespuestaGenerica>> = MutableLiveData()

    fun registrarUsuario(
        inputUsuario: TextInputLayout,
        inputCorreo: TextInputLayout,
        inputPassword: TextInputLayout,
        inputRol: SwitchMaterial
    ) {

        val rol = if (inputRol.isChecked) {
            2
        } else {
            1
        }

        val usuario = CuerpoRegistro(
            inputUsuario.editText!!.text.toString(),
            inputCorreo.editText!!.text.toString(),
            inputPassword.editText!!.text.toString(),
            rol
        )

        viewModelScope.launch {
            val respuestaAPI = repository.registrarUsuario(usuario)

            respuestaGenerica.value = respuestaAPI
        }
    }
}