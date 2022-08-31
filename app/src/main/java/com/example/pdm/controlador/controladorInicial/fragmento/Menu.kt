package com.example.pdm.controlador.controladorInicial.fragmento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdm.R
import com.example.pdm.util.claseBase.FragmentoBase
import kotlinx.android.synthetic.main.fragmento_menu_inicial.view.*

class Menu : FragmentoBase() {
    // Instanciamos los fragmentos.
    private lateinit var fragmentoRegistro: Registro
    private lateinit var fragmentoLogin: Login

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inicializando los fragmentos.
        fragmentoRegistro = Registro()
        fragmentoLogin = Login()

        fragmento = inflater.inflate(
            R.layout.fragmento_menu_inicial,
            container,
            false
        )

        fragmento.botonLogin.setOnClickListener {
            cambiarFragmento(fragmentoLogin)
        }

        fragmento.botonRegistro.setOnClickListener {
            cambiarFragmento(fragmentoRegistro)
        }



        return fragmento
    }
}