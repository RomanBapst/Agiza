package com.example.agiza.data.remote.agent

import com.example.agiza.data.agent.AgentService
import com.example.agiza.data.agent.AgentServiceWriter
import com.example.agiza.data.remote.dto.Agent
import com.example.agiza.data.remote.dto.Product
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.coroutines.coroutineContext


class RemoteAgentServiceImpl(private val supabaseClient : SupabaseClient) : AgentServiceWriter,
    AgentService {
    override val agentWriter = MutableStateFlow<List<Agent>>(emptyList())
    override val agents = agentWriter.asStateFlow()
    var job : Job? = null


    @OptIn(SupabaseExperimental::class)
    override suspend fun connect() {
        with(CoroutineScope(coroutineContext)) {
            job = launch {

                try {
                    supabaseClient.from("agents").selectAsFlow(Agent::id).collect() {
                        agentWriter.value = it
                        println("agent: $it")
                    }
                } catch (exception: Exception) {
                    println(exception.message)
                }
            }
        }

    }

    override suspend fun disconnect() {
        job?.cancel()
    }

}
