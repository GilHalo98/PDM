package com.example.pdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.util.PatternsCompat
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class LoginModel : AppCompatActivity() {
    private lateinit var inputCorreo: TextInputLayout
    private lateinit var inputPassword: TextInputLayout

    private lateinit var botonLogIn: Button
    private lateinit var botonSingIn: Button

    // Validamos el Email ingresado por el usuario.
    private fun validarEmail(inputCorreo: TextInputLayout): Boolean {
        if (inputCorreo.editText!!.text.isEmpty()) {
            inputCorreo.error = getString(R.string.campo_vacio)
            return false

        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(inputCorreo.editText!!.text).matches()) {
            inputCorreo.error = getString(R.string.dato_no_correo)
            return false
        }

        inputCorreo.error = null
        return true
    }

    // Validamos la contra ingresada por el usuario.
    private fun validarPassword(inputPassword: TextInputLayout): Boolean {
        val passwordRegex = Pattern.compile(
            "^" +
            "(?=\\S+$)" +          //No espacios en blanco
            ".{8,}" +              //Minimo 8 caracteres
            "$"
        )

        if (inputPassword.editText!!.text.isEmpty()) {
            inputPassword.error = getString(R.string.campo_vacio)
            return false

        } else if (!passwordRegex.matcher(inputPassword.editText!!.text).matches()) {
            inputPassword.error = getString(R.string.dato_no_password)
            return false
        }

        inputPassword.error = null
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputCorreo = findViewById(R.id.inputCorreo)
        inputPassword = findViewById(R.id.inputPassword)

        botonLogIn = findViewById(R.id.botonLogIn)
        botonSingIn = findViewById(R.id.botonSignIn)

        botonLogIn.setOnClickListener {
            validarEmail(inputCorreo)
            validarPassword(inputPassword)

            val toast = Toast.makeText(this, R.string.no_coneccion_api, Toast.LENGTH_LONG)
            toast.show()
        }

        botonSingIn.setOnClickListener {
            val intent = Intent(this, RegistroModel::class.java)
            startActivity(intent)
        }
    }
}
