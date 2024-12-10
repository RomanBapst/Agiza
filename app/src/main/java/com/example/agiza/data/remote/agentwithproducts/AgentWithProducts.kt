package com.example.agiza.data.remote.agentwithproducts

import com.example.agiza.data.agentwithproducts.AgentWithProductsService
import com.example.agiza.data.agentwithproducts.AgentWithProductsWriter
import com.example.agiza.data.remote.dto.Agent
import com.example.agiza.data.remote.dto.Product
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

@Serializable
data class AgentWithProducts(
    val agent : Agent,
    val products: List<Product>
)


class RemoteAgentWithProductsImpl(val supabaseClient: SupabaseClient) : AgentWithProductsService, AgentWithProductsWriter {
    override val writer =  MutableStateFlow<List<AgentWithProducts>>(emptyList())

    override suspend fun connect() {
    }

    override suspend fun disconnect() {
    }

    override val agentsWithProducts = writer.asStateFlow()

    override suspend fun getAgentsWithProducts() : List<AgentWithProducts> {
        return try {
            supabaseClient.postgrest.rpc("get_agents_with_products").decodeAs<List<AgentWithProducts>>()
        } catch(exception : Exception) {
            println(exception.message)
            emptyList<AgentWithProducts>()
        }
    }

}
