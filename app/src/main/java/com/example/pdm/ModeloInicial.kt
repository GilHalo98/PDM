package com.example.pdm

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.example.pdm.controlador.controladorInicial.fragmento.Menu
import com.example.pdm.util.claseBase.ModeloBase

class ModeloInicial : ModeloBase() {

    /*
        TODO: Resolver bug en donde al cambiar de fragmento, el teclado se queda en pantalla.
    */

    // Declaracion de fragmentos de la vista
    private lateinit var fragmentoMenu: Menu

    // Intent usado para realizar refresh del modelo.
    private lateinit var refresh: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos los fragmentos.
        fragmentoMenu = Menu()

        // Establecemos el layout de la vista.
        setContentView(R.layout.vista_inicial)

        // Establecemos el primer fragmento a mostrar.
        cambiarFragmento(fragmentoMenu)

        // Instancia del refresh.
        refresh = Intent(this, ModeloInicial::class.java)
    }

    override fun onRestart() {
        super.onRestart()

        // Inicializamos los fragmentos.
        fragmentoMenu = Menu()

        // Establecemos el layout de la vista.
        setContentView(R.layout.vista_inicial)

        // Establecemos el primer fragmento a mostrar.
        cambiarFragmento(fragmentoMenu)
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        val fragmento = menuInflater

        fragmento.inflate(R.menu.menu_actionbar_inicial, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.idioma_defautl -> {
                cambiarIdioma(refresh, "es", "mx")
            }

            R.id.idioma_ingles -> {
                cambiarIdioma(refresh, "en", "usa")
            }
        }

        return super.onOptionsItemSelected(item)
    }
}