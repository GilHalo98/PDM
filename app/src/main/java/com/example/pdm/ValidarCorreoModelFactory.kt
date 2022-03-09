package com.example.pdm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdm.repository.Repository

class ValidarCorreoModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ValidarCorreoPresentador(repository) as T
    }
}