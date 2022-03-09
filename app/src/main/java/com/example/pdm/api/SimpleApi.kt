package com.example.pdm.api

import com.example.pdm.model.RespuestaRegistro
import com.example.pdm.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SimpleApi {
    @GET("usuario/validar/{correo}/{codigo}")
    suspend fun validarCorreo(
        @Path("correo") correo: String,
        @Path("codigo") codigo: String
    ): Response<RespuestaRegistro>

    @GET("usuario/validar/{correo}")
    suspend fun enviarValidacion(
        @Path("correo") correo: String
    ): Response<RespuestaRegistro>

    @POST("usuario/registrar/")
    suspend fun registrarUsuario(
        @Body usuario: Usuario
    ): Response<RespuestaRegistro>
}