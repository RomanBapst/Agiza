package com.example.agiza.data.agent

import com.example.agiza.data.remote.dto.Agent
import com.example.agiza.data.remote.dto.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.JsonArray

enum class ProductRefreshState {
    REFRESHING,
    REFRESHED,
    ERROR

}

interface AgentService {
    suspend fun connect()
    suspend fun disconnect()
    fun isConnected() : Boolean
    suspend fun addProduct(name: String, price: Float)
    suspend fun refreshProducts()

    val agents : StateFlow<List<Agent>>
    val products : StateFlow<List<Product>>
    val productRefreshState : StateFlow<ProductRefreshState>
}

interface AgentServiceWriter {
    val agentWriter : MutableStateFlow<List<Agent>>
    val productWriter : MutableStateFlow<List<Product>>
}
