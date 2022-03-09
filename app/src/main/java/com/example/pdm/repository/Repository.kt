package com.example.pdm.repository

import com.example.pdm.api.RetrofitInstance
import com.example.pdm.model.RespuestaRegistro
import com.example.pdm.model.Usuario
import retrofit2.Response

class Repository {
    suspend fun registrarUsuario(usuario: Usuario): Response<RespuestaRegistro> {
        return RetrofitInstance.api.registrarUsuario(usuario)
    }

    suspend fun enviarValidacion(correo: String): Response<RespuestaRegistro> {
        return RetrofitInstance.api.enviarValidacion(correo)
    }

    suspend fun validarCorreo(correo: String, codigo: String): Response<RespuestaRegistro> {
        return RetrofitInstance.api.validarCorreo(correo, codigo)
    }
}