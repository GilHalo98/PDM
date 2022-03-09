package com.example.pdm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.repository.Repository
import com.google.android.material.textfield.TextInputLayout
import androidx.lifecycle.Observer

class ValidarCorreoModel : AppCompatActivity()  {
    private lateinit var botonValidar: Button
    private lateinit var botonReenviar: Button
    private lateinit var inputCodigo: TextInputLayout

    // Prueba de coneccion con la API
    private lateinit var presentador: ValidarCorreoPresentador

    override fun onCreate(savedInstanceState: Bundle?) {
        var correo = intent.getStringExtra("correo")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validar_correo)

        inputCodigo = findViewById(R.id.inputCodigo)

        botonValidar = findViewById(R.id.botonValidar)
        botonReenviar = findViewById(R.id.botonReenviar)

        val repo = Repository()
        val vmf = ValidarCorreoModelFactory(repo)
        presentador = ViewModelProvider(this, vmf).get(ValidarCorreoPresentador::class.java)

        botonValidar.setOnClickListener {

            presentador.verificarCorreo(inputCodigo, correo)

            presentador.respuestaVerificacion.observe(this, Observer { respuesta ->
                Toast.makeText(this, respuesta.message, Toast.LENGTH_LONG).show()
            })
        }

        botonReenviar.setOnClickListener {
            presentador.reenviarCorreo(correo)

            presentador.respuestaReenvio.observe(this, Observer { respuesta ->
                Toast.makeText(this, respuesta.message, Toast.LENGTH_LONG).show()
            })
        }
    }
}