package com.example.pdm.controlador.controladorPrincipal.fragmentoFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.controlador.controladorPrincipal.presentadorFragmento.PresentadorInterfaz
import com.example.pdm.repository.Repository

class InterfazFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PresentadorInterfaz(repository) as T
    }
}