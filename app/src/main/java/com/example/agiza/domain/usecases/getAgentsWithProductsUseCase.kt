package com.example.agiza.domain.usecases

import com.example.agiza.Repository
import com.example.agiza.data.remote.dto.Agent
import com.example.agiza.domain.models.AgentModel
import com.example.agiza.domain.models.ProductModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.coroutineContext

data class AgentWithProducts(
    val agent: AgentModel,
    val products: List<ProductModel>
)
