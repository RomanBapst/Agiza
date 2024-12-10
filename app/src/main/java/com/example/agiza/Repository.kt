package com.example.agiza

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.agiza.components.authentication.AuthenticationService
import com.example.agiza.components.product.ProductService
import com.example.agiza.components.shops.ShopsService
import com.example.agiza.data.agent.AgentService
import com.example.agiza.data.agentwithproducts.AgentWithProductsService
import com.example.agiza.data.remote.agentwithproducts.AgentWithProducts
import com.example.agiza.data.remote.agentwithproducts.RemoteAgentWithProductsImpl
import kotlinx.coroutines.launch


class Repository(
    private val authenticator: AuthenticationService,
    private val shopsService: ShopsService,
    private val productsService: ProductService,
    private val agentService : AgentService,
    private val agentWithProductService: AgentWithProductsService,
    lifecycle: Lifecycle,
) : DefaultLifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    override fun onPause(owner: LifecycleOwner) {
        owner.lifecycleScope.launch {
            shopsService.disconnect()
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        owner.lifecycleScope.launch {
            shopsService.connect()
            agentService.connect()
        }
    }

    val userName = authenticator.authenticationData.userName
    val authenticationState = authenticator.authenticationData.authenticationState
    val role = authenticator.authenticationData.role

    val shops = shopsService.shops
    val products = productsService.products
    val agents = agentService.agents


    suspend fun getAgentsWithProducts() : List<AgentWithProducts> {
        return agentWithProductService.getAgentsWithProducts()
    }

    suspend fun login(ctx: Context) {
        authenticator.authenticate(ctx)
    }

    suspend fun logout(context: Context) {
        authenticator.logout(context)
    }

    suspend fun initCredentials() {
        authenticator.initCredentials()
    }

    suspend fun loginWithEmailAndPassword(email: String, password: String) {
        authenticator.loginWithEmailAndPassword(email, password)
    }
}
