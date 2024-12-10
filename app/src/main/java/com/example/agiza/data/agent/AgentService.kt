package com.example.agiza.data.agent

import com.example.agiza.data.remote.dto.Agent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.JsonArray

interface AgentService {
    suspend fun connect()
    suspend fun disconnect()
    val agents : StateFlow<List<Agent>>
}

interface AgentServiceWriter {
    val agentWriter : MutableStateFlow<List<Agent>>
}
