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

    override val authenticationData : AuthenticationData = AuthenticationDataImpl()
    private val authenticationDataWriter = authenticationData as AuthenticationDataWriter

}
