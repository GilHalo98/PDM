package com.example.pdm.controlador.controladorInicial.fragmentoFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.controlador.controladorInicial.presentadorFragmento.PresentadorRegistro
import com.example.pdm.repository.Repository

class RegistroFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PresentadorRegistro(repository) as T
    }
}