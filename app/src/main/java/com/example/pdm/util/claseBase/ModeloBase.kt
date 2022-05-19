package com.example.pdm.util.claseBase

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pdm.R
import com.example.pdm.util.Constants
import io.github.nefilim.kjwt.JWSHMAC256Algorithm
import io.github.nefilim.kjwt.verifySignature
import java.util.*

open class ModeloBase : AppCompatActivity() {
    fun valorToken(token: String, propiedad: String): String {
        // Retorna un valor de una propiedad dada del token.
        var valor = ""
        verifySignature<JWSHMAC256Algorithm>(token, Constants.SECRET).tap {
            valor = it.claimValue(propiedad).exists { contenido ->
                return contenido
            }.toString()
        }

        return valor
    }

    fun cambiarActivity(intent: Intent) {
        // Cambia la activity a una dada.
        startActivity(intent)
    }

    fun cambiarFragmento(fragmento: Fragment) = supportFragmentManager.beginTransaction(
    ).apply {
        // Cmbia el fragmento a uno dado.
        replace(R.id.flFragment, fragmento)
        commit()
    }

    fun cambiarIdioma(refresh: Intent, idioma: String, pais: String) {
        // Reinicia la activity y cambia el local.
        val locale = Locale(idioma, pais)
        Locale.setDefault(locale)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(locale)
        res.updateConfiguration(conf, dm)
        finish()
        startActivity(refresh)
    }

    fun mostrarToast(contexto: Context, texto: String) {
        // Muesra un toast con un string dado.
        Toast.makeText(contexto, texto, Toast.LENGTH_SHORT).show()
    }
}