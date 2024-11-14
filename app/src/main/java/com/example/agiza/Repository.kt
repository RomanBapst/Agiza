package com.example.agiza

import android.content.Context
import com.example.agiza.components.authentication.AuthenticationService


class Repository(private val authenticator : AuthenticationService) {
    val userName = authenticator.authenticationData.userName
    val authenticationState = authenticator.authenticationData.authenticationState

    suspend fun login(ctx: Context) {
        authenticator.authenticate(ctx)
    }

}