package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.ModeloInicial
import com.example.pdm.ModeloPrincipal
import com.example.pdm.R
import com.example.pdm.controlador.controladorPrincipal.fragmentoFactory.InterfazFactory
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorInterfaz
import com.example.pdm.repository.Repository
import com.example.pdm.util.claseBase.FragmentoBase

class Interfaz : FragmentoBase() {
    // Inicializamos componentes del fragmento.
    private lateinit var spinnerLenguajes: Spinner
    private lateinit var botonCambiarIdioma: Button

    // Presentador del fragmento.
    private lateinit var presentador: PresentadorInterfaz

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Instanciamos las variables de cambios.
        var nuevoIdioma = -1

        // Recuperamos el paquede de datos pasados.
        paqueteDatos = requireArguments()

        // Recuperamos el token de la sesion.
        val token = paqueteDatos.get("token").toString()

        // Elementos del fragmento.
        fragmento = inflater.inflate(
            R.layout.fragmento_configuracion_interfaz,
            container,
            false
        )

        // Instanciamos la lista de lenguajes.
        val listaLenguajes = resources.getStringArray(R.array.lenguajes)

        // Instanciamos el contexto de la app.
        val contexto = requireContext().applicationContext

        // Instanciamos los componentes.
        spinnerLenguajes = fragmento.findViewById(R.id.spinnerCambiarIdioma)
        botonCambiarIdioma = fragmento.findViewById(R.id.botonCambiarIdioma)

        // Instanciamos un adaptador con un layout simple.
        val adaptador = ArrayAdapter(
            contexto,
            android.R.layout.simple_spinner_item,
            listaLenguajes
        )
        // A esto le puedo agregar mi adaptador propio, creo que es lo mismo que con un recycleView
        // Solo que seria con SpinnerAdapter<T>()

        // Asignamos el adaptador al spinner.
        spinnerLenguajes.adapter = adaptador

        // Instanciamos el repositorio de la api.
        val repo = Repository()

        // Instanciamos el factory del fragmento.
        val factoryFragment = InterfazFactory(repo)

        // Instanciamos el presentador del fragmento
        presentador = ViewModelProvider(
            this,
            factoryFragment
        ).get(PresentadorInterfaz::class.java)


        // Manejamos el evento del spinner.
        spinnerLenguajes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                nuevoIdioma = position
            }
        }
        // Nuevamente, esto se puede implementar directamente sobre el adaptador personalizado.

        // Escuchamos por el boton de realizar cambios.
        botonCambiarIdioma.setOnClickListener {
            // Realiza la peticion de cambio de idioma.
            presentador.cambiarIdiomaInterfaz(token, nuevoIdioma)

            // Mostramos si el cambio se realizo o no.
            presentador.establecerCambio(viewLifecycleOwner, contexto, this)
        }

        return fragmento
    }
}