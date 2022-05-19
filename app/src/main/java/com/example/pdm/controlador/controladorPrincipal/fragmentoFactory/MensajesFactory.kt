package com.example.pdm.controlador.controladorPrincipal.fragmentoFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorMensajes
import com.example.pdm.repository.Repository

class MensajesFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PresentadorMensajes(repository) as T
    }
}