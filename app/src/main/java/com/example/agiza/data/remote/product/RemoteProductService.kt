package com.example.agiza.data.remote.product

import com.example.agiza.data.product.ProductService
import com.example.agiza.data.product.ProductServiceWriter
import com.example.agiza.data.remote.dto.Product
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class RemoteProductServiceImpl(val supabaseClient: SupabaseClient) : ProductServiceWriter, ProductService {
    override val productWriter = MutableStateFlow<List<Product>>(emptyList())
    override val products = productWriter.asStateFlow()
    var job : Job? = null
    @OptIn(SupabaseExperimental::class)
    override suspend fun connect() {
        with(CoroutineScope(coroutineContext)) {
           job = launch {
               try {
                   supabaseClient.from("product").selectAsFlow(Product::id).collect() {
                       productWriter.value = it
                   }
               } catch (exception : Exception) {
                   println(exception.message)
               }
           }
        }
    }

    override suspend fun disconnect() {
        job?.cancel()
    }
}