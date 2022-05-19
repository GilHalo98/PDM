package com.example.pdm.util.claseBase

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.pdm.util.Constants
import io.github.nefilim.kjwt.JWSHMAC256Algorithm
import io.github.nefilim.kjwt.JWT
import io.github.nefilim.kjwt.sign
import io.github.nefilim.kjwt.verifySignature
import okhttp3.Headers
import java.time.Clock
import java.time.LocalDateTime

open class PresentadorBase(): ViewModel() {
    fun getHeader(headers: Headers, header: String): String {
        // Retorna un header de la respuesta del request.
        return headers.get(header).toString()
    }

    fun valorToken(token: String, propiedad: String): String {
        // Retorna un valor de una propiedad dada del token.
        var valor = ""
        verifySignature<JWSHMAC256Algorithm>(token, Constants.SECRET).tap {
            valor = it.claimValue(propiedad).toString()
        }

        return valor
    }

    fun crearToken(correo: String, password: String): String {
        val horaActual = LocalDateTime.now(Clock.systemUTC())

        val jwt = JWT.hs256(null) {
            claim("correo", correo)
            claim("password", password)

            expiresAt(horaActual.plusHours(1))
            issuedNow()
        }

        val firma = jwt.sign(Constants.SECRET)
        var token = ""
        firma.tap {
            token = it.rendered
        }

        return token
    }

    fun mostrarToast(contexto: Context, texto: String) {
        // Muesra un toast con un string dado.
        Toast.makeText(contexto, texto, Toast.LENGTH_SHORT).show()
    }
}