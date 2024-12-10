package com.example.agiza.data.product

import com.example.agiza.data.remote.dto.Agent
import com.example.agiza.data.remote.dto.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.JsonArray

interface ProductService {
    suspend fun connect()
    suspend fun disconnect()
    val products : StateFlow<List<Product>>
}

interface ProductServiceWriter {
    val productWriter: MutableStateFlow<List<Product>>
}
