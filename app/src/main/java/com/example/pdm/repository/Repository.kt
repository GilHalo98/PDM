package com.example.pdm.repository

import com.example.pdm.api.RetrofitInstance
import com.example.pdm.modelos.*
import retrofit2.Response

class Repository {
    suspend fun registrarUsuario(usuario: CuerpoRegistro): Response<RespuestaGenerica> {
        return RetrofitInstance.api.registrarUsuario(usuario)
    }

    suspend fun enviarValidacion(correo: String): Response<RespuestaGenerica> {
        return RetrofitInstance.api.enviarValidacion(correo)
    }

    suspend fun validarCorreo(correo: String, codigo: String): Response<RespuestaGenerica> {
        return RetrofitInstance.api.validarCorreo(correo, codigo)
    }

    suspend fun login(cuerpo: CuerpoLogin): Response<RespuestaGenerica> {
        return RetrofitInstance.api.login(cuerpo)
    }

    suspend fun datosUsuario(token: String): Response<RespuestaDatosUsuario> {
        return RetrofitInstance.api.datosUsuario(token)
    }

    suspend fun contactosUsuario(token: String): Response<RespuestaContactosUsuario> {
        return RetrofitInstance.api.contactosUsuario(token)
    }

    suspend fun busquedaUsuario(token: String, correo: String): Response<RespuestaContactosUsuario> {
        return RetrofitInstance.api.busquedaUsuario(token, correo)
    }

    suspend fun agregarContacto(token: String, cuerpo: AgregarContacto): Response<RespuestaGenerica> {
        return RetrofitInstance.api.agregarContacto(token, cuerpo)
    }

    suspend fun cambiarIdioma(token: String, cuerpo: CambioIdioma): Response<RespuestaGenerica> {
        return RetrofitInstance.api.cambiarIdioma(token, cuerpo)
    }

    suspend fun cambiarPassword(token: String, cuerpo: CambioPassword): Response<RespuestaGenerica> {
        return RetrofitInstance.api.cambiarPassword(token, cuerpo)
    }

    suspend fun cambiarEstado(token: String, cuerpo: CambioEstado): Response<RespuestaGenerica> {
        return RetrofitInstance.api.cambiarEstado(token, cuerpo)
    }

    suspend fun cambiarEmail(token: String, cuerpo: CambioEmail): Response<RespuestaGenerica> {
        return RetrofitInstance.api.cambiarEmail(token, cuerpo)
    }

    suspend fun cambiarUsername(token: String, cuerpo: CambioUsername): Response<RespuestaGenerica> {
        return RetrofitInstance.api.cambiarUsername(token, cuerpo)
    }

    suspend fun listarUsuarios(token: String, correo: String): Response<ListaUsuariosAdmin> {
        return RetrofitInstance.api.listarUsuarios(token, correo)
    }

    suspend fun eliminarUsuario(token: String, cuerpo: EliminarUsuarioAdmin): Response<RespuestaGenerica> {
        return RetrofitInstance.api.eliminarUsuario(token, cuerpo)
    }
}