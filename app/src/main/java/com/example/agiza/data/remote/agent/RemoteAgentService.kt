package com.example.agiza.data.remote.agent

import com.example.agiza.data.agent.AgentService
import com.example.agiza.data.agent.AgentServiceWriter
import com.example.agiza.data.agent.ProductRefreshState
import com.example.agiza.data.remote.dto.AddProduct
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.coroutines.coroutineContext


class RemoteAgentServiceImpl(private val supabaseClient : SupabaseClient) : AgentServiceWriter,
    AgentService {
    override val agentWriter = MutableStateFlow<List<Agent>>(emptyList())
    override val productWriter = MutableStateFlow<List<Product>>(emptyList())
    override val agents = agentWriter.asStateFlow()
    override val products = productWriter.asStateFlow()
    val _productRefreshState = MutableStateFlow(ProductRefreshState.REFRESHED)
    override val productRefreshState = _productRefreshState.asStateFlow()


    override suspend fun connect() {

    }

    override suspend fun disconnect() { } override fun isConnected() : Boolean { return false }


    override suspend fun addProduct(name: String, price: Float) {
        try {
            supabaseClient.from("product").insert(AddProduct(name = name, price = price, payload = null, available = true))
        } catch (exception : Exception) {
            println("expection is : " + exception)
        }
    }

    override suspend fun refreshProducts() {
        _productRefreshState.value = ProductRefreshState.REFRESHING
        try {
            val res = supabaseClient.from("product").select().decodeList<Product>()
            productWriter.value = res
        } catch (e : Exception) {
            println(e.message)
            _productRefreshState.value = ProductRefreshState.ERROR
            return
        }
        _productRefreshState.value = ProductRefreshState.REFRESHED
    }
}
