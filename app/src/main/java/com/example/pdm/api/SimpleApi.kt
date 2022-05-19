package com.example.pdm.api

import com.example.pdm.modelos.*
import retrofit2.Response
import retrofit2.http.*

interface SimpleApi {
    // Valida un correo electronico de un usuario registrado.
    @GET("usuario/validar/{correo}/{codigo}/asLogin=true")
    suspend fun validarCorreo(
        @Path("correo") correo: String,
        @Path("codigo") codigo: String
    ): Response<RespuestaGenerica>

    // Realiza una peticion para reenviar un correo de validacion.
    @GET("usuario/validar/{correo}")
    suspend fun enviarValidacion(
        @Path("correo") correo: String
    ): Response<RespuestaGenerica>

    // Registra un usuario a la API.
    @POST("usuario/registrar/")
    suspend fun registrarUsuario(
        @Body usuario: CuerpoRegistro
    ): Response<RespuestaGenerica>

    // Realiza un login con la API.
    @POST("usuario/login/")
    suspend fun login(
        @Body cuerpo: CuerpoLogin
    ): Response<RespuestaGenerica>

    // Consulta la preferencia del usuario.
    @GET("preferencia/datos/")
    suspend fun datosUsuario(
        @Header("token") token: String
    ): Response<RespuestaDatosUsuario>

    // Consulta la lista de contactos del usuario.
    @GET("usuarios/contactos/")
    suspend fun contactosUsuario(
        @Header("token") token: String
    ): Response<RespuestaContactosUsuario>

    // Consulta usuarios dado un correo electronico.
    @GET("usuarios/buscar/{correo}/addContacts=true")
    suspend fun busquedaUsuario(
        @Header("token") token: String,
        @Path("correo") correo: String,
    ): Response<RespuestaContactosUsuario>

    // Agrega un usuario a la lista de contactos del usuario.
    @POST("usuarios/contactos/agregar/")
    suspend fun agregarContacto(
        @Header("token") token: String,
        @Body cuerpo: AgregarContacto,
    ): Response<RespuestaGenerica>

    // Cambia el idioma de las preferenias del usuario.
    @PUT("preferencia/cambiar/idioma/")
    suspend fun cambiarIdioma(
        @Header("token") token: String,
        @Body cuerpo: CambioIdioma,
    ): Response<RespuestaGenerica>

    // Cambia la password del usuario.
    @PUT("preferencia/cambiar/password/")
    suspend fun cambiarPassword(
        @Header("token") token: String,
        @Body cuerpo: CambioPassword,
    ): Response<RespuestaGenerica>

    // Cambia el estado del perfil del usuario.
    @PUT("preferencia/cambiar/estado/")
    suspend fun cambiarEstado(
        @Header("token") token: String,
        @Body cuerpo: CambioEstado,
    ): Response<RespuestaGenerica>

    // Cambiar el correo electronico del usuario.
    @PUT("preferencia/cambiar/email/")
    suspend fun cambiarEmail(
        @Header("token") token: String,
        @Body cuerpo: CambioEmail,
    ): Response<RespuestaGenerica>

    //  Cambia el nombre de usuario.
    @PUT("preferencia/cambiar/username/")
    suspend fun cambiarUsername(
        @Header("token") token: String,
        @Body cuerpo: CambioUsername,
    ): Response<RespuestaGenerica>
}