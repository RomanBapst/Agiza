package com.example.agiza.components.agent.product

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.agiza.JetpackComposeApp
import com.example.agiza.Repository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


sealed class AddProductUiEvent {
    data object NavigateToProducts : AddProductUiEvent()
}

class AddProductViewModel(private val repository: Repository) : ViewModel() {
    val _uiEvent = Channel<AddProductUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
   val _productName = MutableStateFlow("")
   val productName = _productName.asStateFlow()
    val _productPrice = MutableStateFlow(0.0f)
    val productPrice = _productPrice.asStateFlow()

    fun addProduct(name: String, price: Float) {
        viewModelScope.launch {
            repository.addProduct(name, price)
            repository.refreshProducts()
            _uiEvent.send(AddProductUiEvent.NavigateToProducts)
        }
    }

    fun nameChanged(name: String) {
        _productName.value = name
    }

    fun priceChanged(text: String) {
        _productPrice.value = text.toFloat()
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as JetpackComposeApp)
                val repository = application.container.repository
                AddProductViewModel(repository)
            }
        }
    }
}
