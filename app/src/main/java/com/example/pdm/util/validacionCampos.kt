package com.example.pdm.util

import android.content.Context
import androidx.core.util.PatternsCompat
import com.example.pdm.R
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class validacionCampos {
    fun validarEmail(contexto: Context, inputCorreo: TextInputLayout): Boolean {
        if (inputCorreo.editText!!.text.isEmpty()) {
            inputCorreo.error = contexto.getString(R.string.campo_vacio)
            return false

        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(inputCorreo.editText!!.text).matches()) {
            inputCorreo.error = contexto.getString(R.string.dato_no_correo)
            return false
        }

        inputCorreo.error = null
        return true
    }

    fun validarLoginPassword(contexto: Context, inputPassword: TextInputLayout): Boolean {
        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=\\S+$)" +          //No espacios en blanco
                    ".{8,}" +              //Minimo 8 caracteres
                    "$"
        )

        if (inputPassword.editText!!.text.isEmpty()) {
            inputPassword.error = contexto.getString(R.string.campo_vacio)
            return false

        } else if (!passwordRegex.matcher(inputPassword.editText!!.text).matches()) {
            inputPassword.error =  contexto.getString(R.string.dato_no_password)
            return false
        }

        inputPassword.error = null
        return true
    }

    fun validarPassword(contexto: Context, inputPassword: TextInputLayout, inputConfirmarPassword: TextInputLayout): Boolean {
        inputPassword.error = null
        inputConfirmarPassword.error = null
        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //Por lo menos un numero
                    "(?=.*[a-z])" +         //Por lo menos 1 letra en minuscula
                    "(?=.*[A-Z])" +         //Por lo menos 1 letra en mayuscual
                    "(?=\\S+$)" +           //No espacios en blanco
                    ".{8,}" +               //Minimo 8 caracteres
                    "$"
        )

        if (inputPassword.editText!!.text.isEmpty()) {
            inputPassword.error = contexto.getString(R.string.campo_vacio)
            return false

        } else if (inputConfirmarPassword.editText!!.text.isEmpty()) {
            inputConfirmarPassword.error = contexto.getString(R.string.campo_vacio)
            return false

        } else if (inputPassword.editText!!.text.length < 8) {
            inputPassword.error = contexto.getString(R.string.dato_no_password)
            return false

        } else if (inputConfirmarPassword.editText!!.text.length < 8) {
            inputConfirmarPassword.error = contexto.getString(R.string.dato_no_password)
            return false

        } else if (inputPassword.editText!!.text.toString() != inputConfirmarPassword.editText!!.text.toString()) {
            inputConfirmarPassword.error = contexto.getString(R.string.password_diferente)
            return false

        } else if (!passwordRegex.matcher(inputPassword.editText!!.text).matches()) {
            inputPassword.error = contexto.getString(R.string.password_debil)
            return false

        } else if (!passwordRegex.matcher(inputConfirmarPassword.editText!!.text).matches()) {
            inputConfirmarPassword.error = contexto.getString(R.string.password_debil)
            return false
        }

        return true
    }

    fun validarUsername(contexto: Context, inputUsername: TextInputLayout): Boolean {
        inputUsername.error = null

        val passwordRegex = Pattern.compile(
            "^" +
                    ".{3,10}" +               //Tiene que haber de 3 a 10 caracteres
                    "$"
        )

        if (inputUsername.editText!!.text.isEmpty()) {
            inputUsername.error = contexto.getString(R.string.campo_vacio)
            return false

        } else if (inputUsername.editText!!.text.length > 10) {
            inputUsername.error = contexto.getString(R.string.username_largo)
            return false

        } else if (inputUsername.editText!!.text.length < 3) {
            inputUsername.error = contexto.getString(R.string.username_corto)
            return false

        } else if (!passwordRegex.matcher(inputUsername.editText!!.text).matches()) {
            inputUsername.error = contexto.getString(R.string.username_invalido)
            return false
        }

        return true
    }

    fun validarCodigoVerificacion(contexto: Context, inputCodigo: TextInputLayout): Boolean {
        inputCodigo.error = null

        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //Por lo menos un numero
                    ".{4,4}" +               //Tiene que haber 4 caracteres
                    "$"
        )

        if (inputCodigo.editText!!.text.isEmpty()) {
            inputCodigo.error = contexto.getString(R.string.campo_vacio)
            return false

        } else if (inputCodigo.editText!!.text.length != 4) {
            inputCodigo.error = contexto.getString(R.string.longitud_codigo)
            return false

        } else if (!passwordRegex.matcher(inputCodigo.editText!!.text).matches()) {
            inputCodigo.error = contexto.getString(R.string.codigo_invalido)
            return false
        }

        return true
    }
}