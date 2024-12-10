package com.example.agiza.components.shops

import android.content.Context
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.maplibre.android.geometry.LatLng
import kotlin.coroutines.coroutineContext

@Serializable
data class Shop(val id: String, val name : String, val lat: Double, val lon: Double)

data class Product(val name: String)

interface ShopsService {
    val shops : StateFlow<List<Shop>>

    suspend fun connect()
    suspend fun disconnect()
}

@OptIn(SupabaseExperimental::class)
class ShopsImpl(val supabase: SupabaseClient) : ShopsService {
    override val shops = MutableStateFlow(emptyList<Shop>())
    var job : Job? = null

    override suspend fun connect() {
        with(CoroutineScope(kotlin.coroutines.coroutineContext)){
            job = launch {
                try {
                    val flow: Flow<List<Shop>> = supabase.from("shops").selectAsFlow(Shop::id)
                    flow.collect {
                        shops.value = it
                    }
                } catch (error: Exception) {
                    println(error)
                }
            }

        }
    }

    override suspend fun disconnect() {
        job?.cancel()
    }
}