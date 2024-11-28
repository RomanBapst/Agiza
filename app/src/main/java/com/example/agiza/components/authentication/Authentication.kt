package com.example.agiza.components.authentication

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AuthenticationState {
    Unauthenticated,
    Authenticating,
    AuthenticatedWaitingEmailConfirmation,
    Authenticated
}

enum class Role {
    USER,
    AGENT,
    ADMIN
}

interface AuthenticationService {
    suspend fun authenticate(context: Context)
    suspend fun initCredentials()
    suspend fun logout(context: Context)
    val authenticationData : AuthenticationData
}

interface AuthenticationData {
    val authenticationState : StateFlow<AuthenticationState>
    val userName : StateFlow<String>
    val role : StateFlow<Role>
}

interface AuthenticationDataWriter {
    val authenticationStateWriter : MutableStateFlow<AuthenticationState>
    val userNameWriter : MutableStateFlow<String>
    val roleWriter : MutableStateFlow<Role>
}

class AuthenticationDataImpl : AuthenticationData, AuthenticationDataWriter {
    override val userNameWriter = MutableStateFlow("")
    override val roleWriter = MutableStateFlow<Role>(Role.USER)
    override val userName = userNameWriter.asStateFlow()
    override val role = roleWriter.asStateFlow()
    override val authenticationStateWriter = MutableStateFlow(AuthenticationState.Unauthenticated)
    override val authenticationState = authenticationStateWriter.asStateFlow()
}
