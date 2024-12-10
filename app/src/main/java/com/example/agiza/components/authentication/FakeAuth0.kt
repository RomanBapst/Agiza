package com.example.agiza.components.authentication

import android.content.Context
import kotlinx.coroutines.delay

class FakeAuth0Authenticator : AuthenticationService {
    override suspend fun authenticate(context: Context) {
        authenticationDataWriter.authenticationStateWriter.value = AuthenticationState.Authenticating
        authenticationDataWriter.userNameWriter.value = "Roman Bapst"
        delay(2000)
        authenticationDataWriter.authenticationStateWriter.value = AuthenticationState.Authenticated
    }

    override suspend fun initCredentials() {
    }

    override suspend fun logout(context: Context) {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override val authenticationData : AuthenticationData = AuthenticationDataImpl()
    private val authenticationDataWriter = authenticationData as AuthenticationDataWriter

}
