package com.example.pdm.controlador.controladorPrincipal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.repository.Repository

class PrincipalFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PresentadorPrincipal(repository) as T
    }
}