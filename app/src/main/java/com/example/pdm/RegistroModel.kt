package com.example.pdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.model.RespuestaRegistro
import com.example.pdm.repository.Repository
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import java.util.regex.Pattern


class RegistroModel : AppCompatActivity() {
    private lateinit var inputUsuario: TextInputLayout
    private lateinit var inputCorreo: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputConfirmarPassword: TextInputLayout

    private lateinit var botonAceptar: Button
    private lateinit var botonCancelar: Button

    private lateinit var switchLegal: Switch

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
    private fun validarPassword(inputPassword: TextInputLayout, inputConfirmarPassword: TextInputLayout): Boolean {
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
            inputPassword.error = getString(R.string.campo_vacio)
            return false

        } else if (inputConfirmarPassword.editText!!.text.isEmpty()) {
            inputConfirmarPassword.error = getString(R.string.campo_vacio)
            return false

        } else if (inputPassword.editText!!.text.length < 8) {
            inputPassword.error = getString(R.string.dato_no_password)
            return false

        } else if (inputConfirmarPassword.editText!!.text.length < 8) {
            inputConfirmarPassword.error = getString(R.string.dato_no_password)
            return false

        } else if (inputPassword.editText!!.text.toString() != inputConfirmarPassword.editText!!.text.toString()) {
            inputConfirmarPassword.error = getString(R.string.password_diferente)
            return false

        } else if (!passwordRegex.matcher(inputPassword.editText!!.text).matches()) {
            inputPassword.error = getString(R.string.password_debil)
            return false

        } else if (!passwordRegex.matcher(inputConfirmarPassword.editText!!.text).matches()) {
            inputConfirmarPassword.error = getString(R.string.password_debil)
            return false
        }

        return true
    }

    // Prueba de coneccion con la API
    private lateinit var presentador: RegistroPresentador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        inputUsuario = findViewById(R.id.inputUsuario)
        inputCorreo = findViewById(R.id.inputCorreo)
        inputPassword = findViewById(R.id.inputPassword)
        inputConfirmarPassword = findViewById(R.id.inputConfirmarPassword)

        botonAceptar = findViewById(R.id.botonRegistrar)
        botonCancelar = findViewById(R.id.botonCancelar)

        switchLegal = findViewById(R.id.switchLegal)

        val repo = Repository()
        val vmf = RegistroModelFactory(repo)

        botonAceptar.setOnClickListener {
            presentador = ViewModelProvider(this, vmf).get(RegistroPresentador::class.java)

            if (validarEmail(inputCorreo) && validarPassword(inputPassword, inputConfirmarPassword)) {
                presentador.registrarUsuario(inputUsuario, inputCorreo, inputPassword)

                presentador.respuestaRegistro.observe(this, Observer { respuesta ->
                    if (!respuesta.isSuccessful && respuesta.errorBody() != null) {
                        try {
                            val respuestaError = Gson().fromJson(
                                respuesta.errorBody()!!.string(),
                                RespuestaRegistro::class.java
                            )
                            Toast.makeText(this, respuestaError.message, Toast.LENGTH_SHORT).show()

                        } catch (excepcion: Exception) {
                            Log.d("Error en la APP", excepcion.toString())
                        }

                    } else {
                        Toast.makeText(this, respuesta.body()!!.message, Toast.LENGTH_SHORT).show()
                    }

                    if (respuesta.isSuccessful) {
                        val intent = Intent(this, ValidarCorreoModel::class.java)
                        intent.putExtra("correo", inputCorreo.editText!!.text.toString());
                        startActivity(intent)
                    }
                })
            }
        }

        botonCancelar.setOnClickListener {
            val intent = Intent(this, LoginModel::class.java)
            startActivity(intent)
        }
    }
}
