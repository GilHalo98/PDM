package com.example.pdm.controlador.controladorPrincipal.fragmento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.example.pdm.R
import com.example.pdm.util.Constants
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetFragment
import java.net.URL

class VideoChat : JitsiMeetFragment() {
    // Inicializamos componentes del fragmento.
    private lateinit var botonIniciarSala: ImageButton
    private lateinit var inputCodigoSala: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Elementos del fragmento.
        val fragmento = inflater.inflate(
            R.layout.fragmento_video_chat,
            container,
            false
        )

        // Instanciamos el contexto de la app.
        val contexto = requireContext().applicationContext

        // Instanciamos los componentes del fragmento.
        botonIniciarSala = fragmento.findViewById(R.id.botonIniciarSala)
        inputCodigoSala = fragmento.findViewById(R.id.inputCodigoSala)

        val opcionesDefautl = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL(Constants.JITSI_URL))
            .setWelcomePageEnabled(false)
            .build()

        JitsiMeet.setDefaultConferenceOptions(opcionesDefautl)

        // Escuchamos por el evento del click de boton de iniciar a sala.
        botonIniciarSala.setOnClickListener {
            val opcionesSala = JitsiMeetConferenceOptions.Builder()
                .setRoom(inputCodigoSala.text.toString())
                .setWelcomePageEnabled(false)
                .build()

            JitsiMeetActivity.launch(contexto, opcionesSala)
        }

        return fragmento
    }
}