package com.example.pdm.util.claseBase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pdm.R
import com.example.pdm.repository.Repository
import com.example.pdm.util.Constants
import io.github.nefilim.kjwt.JWSHMAC256Algorithm
import io.github.nefilim.kjwt.verifySignature
import kotlinx.android.synthetic.main.fragmento_configuracion.view.*
import kotlinx.android.synthetic.main.fragmento_login.view.*
import kotlinx.android.synthetic.main.fragmento_login.view.mainLayout
import java.util.*

open class FragmentoBase : Fragment() {
    // Propiedades dependientes del fragmento.
    lateinit var fragmento: View

    // Bundle de datos pasados.
    lateinit var paqueteDatos: Bundle

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
        startActivity(intent)
    }

    fun cambiarActivityConIdioma(refresh: Intent, idioma: String, pais: String) {
        val locale = Locale(idioma, pais)
        Locale.setDefault(locale)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(locale)
        res.updateConfiguration(conf, dm)
        activity?.finish()
        startActivity(refresh)
    }

    fun cambiarFragmento(nuevoFragmento: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.flFragment, nuevoFragmento)
            commit()
        }
    }

    fun cambiarIdioma(refresh: Intent, idioma: String, pais: String) {
        val locale = Locale(idioma, pais)
        Locale.setDefault(locale)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(locale)
        res.updateConfiguration(conf, dm)
        activity?.finish()
        startActivity(refresh)
    }

    fun mostrarToast(contexto: Context, texto: String) {
        Toast.makeText(contexto, texto, Toast.LENGTH_SHORT).show()
    }
}