package com.example.agiza

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.example.agiza.components.authentication.Auth0Authenticator
import com.example.agiza.components.authentication.FakeAuth0Authenticator

interface ApplicationContainer {
    val ctx: Context
    val auth0: Auth0
    val authentication: AuthenticationAPIClient
    val credentialsManager: CredentialsManager
    val repository : Repository
}


class DefaultAppContainer(override val ctx: Context, override val auth0: Auth0, override val authentication: AuthenticationAPIClient, override val credentialsManager: CredentialsManager) : ApplicationContainer {
    override val repository = Repository(Auth0Authenticator(auth0, credentialsManager))
    //override val repository = Repository(FakeAuth0Authenticator())
}

class JetpackComposeApp : Application() {
    lateinit var container: ApplicationContainer
    lateinit var authentication: AuthenticationAPIClient
    lateinit var auth0: Auth0
    lateinit var credentialsManager: CredentialsManager
    lateinit var storage: SharedPreferencesStorage
    override fun onCreate() {
        super.onCreate()
        val auth0 : Auth0 = Auth0.getInstance(
            ContextCompat.getString(this, R.string.com_auth0_client_id),
            ContextCompat.getString(this, R.string.com_auth0_domain)
        )

        authentication = AuthenticationAPIClient(auth0)
        storage = SharedPreferencesStorage(this)
        credentialsManager = CredentialsManager(authentication, storage)
        container = DefaultAppContainer(applicationContext, auth0, authentication, credentialsManager)
    }

}
