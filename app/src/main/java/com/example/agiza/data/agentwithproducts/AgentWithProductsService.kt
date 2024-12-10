package com.example.agiza.data.agentwithproducts

import com.example.agiza.data.remote.agentwithproducts.AgentWithProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AgentWithProductsService {
    suspend fun connect()
    suspend fun disconnect()
    val agentsWithProducts : StateFlow<List<AgentWithProducts>>
    suspend fun getAgentsWithProducts() : List<AgentWithProducts>
}

interface AgentWithProductsWriter {
    val writer : MutableStateFlow<List<AgentWithProducts>>
}