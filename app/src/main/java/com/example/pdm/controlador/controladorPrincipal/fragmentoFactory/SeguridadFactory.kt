package com.example.pdm.controlador.controladorPrincipal.fragmentoFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorSeguridad
import com.example.pdm.repository.Repository

class SeguridadFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PresentadorSeguridad(repository) as T
    }
}