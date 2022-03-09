package com.example.pdm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.repository.Repository

class RegistroModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegistroPresentador(repository) as T
    }
}