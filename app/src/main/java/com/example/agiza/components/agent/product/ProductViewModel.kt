package com.example.agiza.components.agent.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.agiza.JetpackComposeApp
import com.example.agiza.Repository
import com.example.agiza.components.home.HomeUiEvent
import com.example.agiza.domain.models.AgentModel
import com.example.agiza.domain.models.ProductModel
import com.example.agiza.domain.usecases.AgentWithProducts
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.utils.ThreadUtils.init

sealed class ProductUiEvent {
    data object NavigateAddProduct : ProductUiEvent()
}

class ProductViewModel(private val repository: Repository) : ViewModel() {

    val agentWithProducts = MutableStateFlow<List<AgentWithProducts>>(emptyList())
    val products = repository.products
    val productRefreshState = repository.productRefreshState
    val _uiEvent = Channel<ProductUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {

        viewModelScope.launch {
            repository.refreshProducts()
            println("dbg: init viewmodel")
        }
    }

    fun refreshProducts() {
        viewModelScope.launch {
            repository.refreshProducts()
        }
    }

    fun addProductClicked() {
       viewModelScope.launch {
           _uiEvent.send(ProductUiEvent.NavigateAddProduct)
       }
    }


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