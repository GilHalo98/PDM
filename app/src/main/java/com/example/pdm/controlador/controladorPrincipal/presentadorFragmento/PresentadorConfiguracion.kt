package com.example.pdm.controlador.controladorPrincipal.presentadorFragmento

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import com.example.pdm.modelos.RespuestaDatosUsuario
import com.example.pdm.modelos.RespuestaGenerica
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.PresentadorBase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragmento_login.view.*
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

    fun mostrarDatos(ciclo: LifecycleOwner, textViewUsername: TextView, textViewEstado: TextView) {
        respuestaPeticion.observe(ciclo) {
            if (it.isSuccessful) {
                textViewUsername.text = it.body()!!.usuario.nombreUsuario
                textViewEstado.text = it.body()!!.preferencia.estadoPerfil
            }
        }
    }
}