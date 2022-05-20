package com.example.pdm

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.controlador.controladorPrincipal.PresentadorPrincipal
import com.example.pdm.controlador.controladorPrincipal.PrincipalFactory
import com.example.pdm.controlador.controladorPrincipal.fragmento.*
import com.example.pdm.repository.Repository
import com.example.pdm.socketIO.SocketInstance
import com.example.pdm.util.claseBase.ModeloBase
import com.google.android.material.bottomnavigation.BottomNavigationView

class ModeloPrincipal : ModeloBase() {
    // Fragmentos.
    private lateinit var fragmentoConfiguracion: Configuracion
    private lateinit var fragmentoHistorial: Historial
    private lateinit var fragmentoVideoChat: VideoChat
    private lateinit var fragmentoMensajes: Mensajes
    private lateinit var fragmentoAdminPanel: AdminPanel

    // Componentes de la vista.
    private lateinit var navegacionPie: BottomNavigationView

    // Bundle de datos pasados.
    private lateinit var paqueteDatos: Bundle

    // Valor del token recivido.
    private lateinit var token: String

    // Presentador de la vista.
    private lateinit var presentador: PresentadorPrincipal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_principal)

        // Instanciamos el repo.
        val repo = Repository()

        // Instanciamos el factory de la vista.
        val factoryActivity = PrincipalFactory(repo)

        // Instanciamos el presentador de la vista.
        presentador = ViewModelProvider(
            this,
            factoryActivity
        ).get(PresentadorPrincipal::class.java)

        // Recupera el token pasado.
        token = intent.getStringExtra("token").toString()

        // Inicializamos el objeto token.
        SocketInstance.crearInstancia(token)

        // Conectamos el cliente del socket.
        SocketInstance.conectar(token)

        // Inicializamos los datos a enviar.
        paqueteDatos = Bundle()

        // Ponemos los datos pasados en un paquete de datos.
        paqueteDatos.putString(
            "token",
            token
        )

        // Instancia de componentes.
        navegacionPie = findViewById(R.id.navegacionPie)

        // Instancia de fragmentos de la vista.
        fragmentoConfiguracion = Configuracion()
        fragmentoHistorial = Historial()
        fragmentoVideoChat = VideoChat()
        fragmentoMensajes = Mensajes()
        fragmentoAdminPanel = AdminPanel()

        // Asignamos el paquete de datos a los fragmentos que los usaran.
        fragmentoConfiguracion.arguments = paqueteDatos
        fragmentoMensajes.arguments = paqueteDatos

        // Establece el fragmento por default.
        cambiarFragmento(fragmentoMensajes)

        // Infla el menu de pie dependiendo del tipo de usuario.
        inflarMenuPie()

        navegacionPie.setOnItemSelectedListener {
            manejarInputMenuPie(it)
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Terminamos el cliente del socket.
        SocketInstance.desconectar(token)
    }

    fun inflarMenuPie() {
        // Dependiendo del rol de usuario, muestra el menu de pie.
        if (valorToken(token, "rol") == "2") {
            navegacionPie.inflateMenu(R.menu.admin_menu_pie)
        } else {
            navegacionPie.inflateMenu(R.menu.menu_pie)
        }
    }

    fun manejarInputMenuPie(item: MenuItem) {
        // Maneja el input del menu de pie.
        when(item.itemId) {
            R.id.configuracion-> {
                cambiarFragmento(fragmentoConfiguracion)
            }

            R.id.historial-> {
                cambiarFragmento(fragmentoVideoChat)
            }

            R.id.mensajes-> {
                cambiarFragmento(fragmentoMensajes)
            }

            R.id.admin-> {
                cambiarFragmento(fragmentoAdminPanel)
            }
        }
    }
}