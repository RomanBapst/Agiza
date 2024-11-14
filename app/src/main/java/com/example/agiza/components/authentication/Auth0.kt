package com.example.agiza.components.authentication

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.provider.WebAuthProvider

class Auth0Authenticator(private val auth0: Auth0, private val credentialsManager: CredentialsManager) :
    AuthenticationService {
    override val authenticationData : AuthenticationData = AuthenticationDataImpl()
    private val authenticationDataWriter = authenticationData as AuthenticationDataWriter
    override suspend fun authenticate(context: Context) {
        authenticationDataWriter.authenticationStateWriter.value = AuthenticationState.Authenticating
        val cred = WebAuthProvider.login(auth0).withScheme("agiza").withTrustedWebActivity().await(context)
        credentialsManager.saveCredentials(cred)
        authenticationDataWriter.userNameWriter.value = cred.user.name ?: ""
        authenticationDataWriter.authenticationStateWriter.value = AuthenticationState.Authenticated
    }

}
