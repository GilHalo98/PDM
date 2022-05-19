package com.example.pdm.util.enumerador

enum class Codigos(id: Int, descripcion: String) {
    API_ERROR(-1, "Error Con la API"),
    LOGIN_OK(0, "Inicio de sesion correcto"),
    INFORMACION_INCOMPLETA(1, "Información incompleta"),
    CORREO_NO_ENCONTRADO(2, "Correo No encontrado"),
    CORREO_NO_VALIDADO(3, "Correo no validado"),
    USUARIO_SIN_ROL(4, "Usuario sin rol"),
    USUARIO_SIN_CONFIGURACIONES(5, "No se encontrarón configuraiones"),
    PASSWORD_INCORRECTA(6, "Contraseña incorrecta"),
    NOMBRE_USUARIO_REGISTRADO(7, "Usuario ya registrado"),
    CORREO_REGISTRADO(8, "Correo ya registrado"),
    CONFIGURACION_DEFAULT_INEXISTENTE(9, "Configuración default no encontrada"),
    REGISTRO_OK(10, "Registro Correcto"),
    CUENTA_YA_VALIDADA(11, "Correo ya validado"),
    CODIGO_DISTITO(12, "Codigo incorrecto"),
    VALIDACION_OK(13, "Correo validado"),
    ERROR_REENVIO_CODIGO(14, "Error al reenviar el código"),
    CODIGO_REENVIADO_OK(15, "Correo de validación enviado"),
    USUARIO_NO_REGISTRADO(16, "Usuario no registrado"),
    DATOS_USUARIO_OK(17, "Vista actualizada"),
}