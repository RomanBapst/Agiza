package com.example.agiza.components.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.agiza.JetpackComposeApp
import com.example.agiza.Repository
import com.example.agiza.domain.models.AgentModel
import com.example.agiza.domain.models.ProductModel
import com.example.agiza.domain.usecases.AgentWithProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng

class ProductViewModel(repository: Repository) : ViewModel() {

    val agentWithProducts = MutableStateFlow<List<AgentWithProducts>>(emptyList())

    init {
        println("creating products viewmodel")
        viewModelScope.launch {
            agentWithProducts.value =  repository.getAgentsWithProducts().map {
                AgentWithProducts(
                    agent = AgentModel(it.agent.id, LatLng(it.agent.lastLatitude!!, it.agent.lastLongitude!!)),
                    products = it.products.map { prod ->
                        ProductModel(prod.id.toString(), prod.name, prod.price)
                    }
                )
            }
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