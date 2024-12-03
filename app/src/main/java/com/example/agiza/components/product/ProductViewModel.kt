package com.example.agiza.components.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.agiza.JetpackComposeApp
import com.example.agiza.Repository
import com.example.agiza.components.home.HomeViewModel

class ProductViewModel(repository: Repository) : ViewModel() {

    val products = repository.products

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as JetpackComposeApp)
                val repository = application.container.repository
                ProductViewModel(repository)
            }
        }
    }
}