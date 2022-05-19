package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdm.R
import com.example.pdm.util.claseBase.FragmentoBase

class Historial : FragmentoBase() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Elementos del fragmento.
        val fragmento = inflater.inflate(R.layout.fragmento_historial, container, false)

        return fragmento
    }
}