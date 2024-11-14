package com.example.agiza.components.authentication

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AuthenticationState {
    Unauthenticated,
    Authenticating,
    Authenticated
}

interface AuthenticationService {
    suspend fun authenticate(context: Context)
    val authenticationData : AuthenticationData
}

interface AuthenticationData {
    val authenticationState : StateFlow<AuthenticationState>
    val userName : StateFlow<String>
}

interface AuthenticationDataWriter {
    val authenticationStateWriter : MutableStateFlow<AuthenticationState>
    val userNameWriter : MutableStateFlow<String>
}

class AuthenticationDataImpl : AuthenticationData, AuthenticationDataWriter {
    override val userNameWriter = MutableStateFlow("")
    override val userName = userNameWriter.asStateFlow()
    override val authenticationStateWriter = MutableStateFlow(AuthenticationState.Unauthenticated)
    override val authenticationState = authenticationStateWriter.asStateFlow()
}
