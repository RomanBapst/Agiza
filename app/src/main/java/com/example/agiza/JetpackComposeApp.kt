package com.example.agiza

import SupabaseAuthentication
import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.example.agiza.components.product.FakeProductsImpl
import com.example.agiza.components.shops.ShopsImpl
import com.example.agiza.components.shops.ShopsService
import com.example.agiza.data.remote.agent.RemoteAgentServiceImpl
import com.example.agiza.data.remote.agentwithproducts.RemoteAgentWithProductsImpl
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

interface ApplicationContainer {
    val ctx: Context
    val auth0: Auth0
    val authentication: AuthenticationAPIClient
    val credentialsManager: CredentialsManager
    val repository: Repository
    val supabase: SupabaseClient
    val lifeCycle: Lifecycle
}


class DefaultAppContainer(
    override val ctx: Context,
    override val auth0: Auth0,
    override val authentication: AuthenticationAPIClient,
    override val credentialsManager: CredentialsManager,
    override val lifeCycle: Lifecycle

) : ApplicationContainer {
    override val supabase = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_PROJECT_URL,
        supabaseKey = BuildConfig.SUPABASE_API_KEY,
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)
        defaultLogLevel = LogLevel.INFO
    }
    val shopsImpl: ShopsService = ShopsImpl(supabase)
    val productsImpl = FakeProductsImpl()
    val lifecycle = ProcessLifecycleOwner.get().lifecycle
    override val repository = Repository(
        SupabaseAuthentication(supabase),
        shopsImpl,
        productsImpl,
        RemoteAgentServiceImpl(supabase),
        RemoteAgentWithProductsImpl(supabase),
        lifecycle,
    )
}

class JetpackComposeApp : Application() {
    lateinit var container: ApplicationContainer
    lateinit var authentication: AuthenticationAPIClient
    lateinit var auth0: Auth0
    lateinit var credentialsManager: CredentialsManager
    lateinit var storage: SharedPreferencesStorage


    override fun onCreate() {
        super.onCreate()
        val auth0: Auth0 = Auth0.getInstance(
            ContextCompat.getString(this, R.string.com_auth0_client_id),
            ContextCompat.getString(this, R.string.com_auth0_domain)
        )

        authentication = AuthenticationAPIClient(auth0)
        storage = SharedPreferencesStorage(this)
        credentialsManager = CredentialsManager(authentication, storage)
        container = DefaultAppContainer(
            applicationContext,
            auth0,
            authentication,
            credentialsManager,
            ProcessLifecycleOwner.get().lifecycle
        )
    }


}
