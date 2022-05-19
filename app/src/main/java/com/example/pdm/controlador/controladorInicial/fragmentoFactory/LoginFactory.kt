package com.example.pdm.controlador.controladorInicial.fragmentoFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.controlador.controladorInicial.presentadorFragmento.PresentadorLogin
import com.example.pdm.repository.Repository

class LoginFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PresentadorLogin(repository) as T
    }
}