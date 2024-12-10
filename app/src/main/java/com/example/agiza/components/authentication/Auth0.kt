package com.example.agiza.components.authentication

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.jwt.JWT
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials

class Auth0Authenticator(
    private val auth0: Auth0,
    private val credentialsManager: CredentialsManager
) :
    AuthenticationService {
    override val authenticationData: AuthenticationData = AuthenticationDataImpl()
    private val authenticationDataWriter = authenticationData as AuthenticationDataWriter


    private suspend fun getStoredCredentials(): Credentials? {
        return if (credentialsManager.hasValidCredentials()) {
            try {
                credentialsManager.awaitCredentials()
            } catch (error: CredentialsManagerException) {
                null
            }
        } else null
    }

    override suspend fun authenticate(context: Context) {
        authenticationDataWriter.authenticationStateWriter.value =
            AuthenticationState.Authenticating

       val credentials =  getStoredCredentials()

        if (credentials != null) {
            checkCredentials(credentials)
        } else {
            credentialsManager.clearCredentials()
            val cred = WebAuthProvider.login(auth0).withScheme("agiza").withAudience("https://agiza/")
                .withScope("profile email offline_access ").withTrustedWebActivity().await(context)
            credentialsManager.saveCredentials(cred)

            checkCredentials(cred)
        }
    }

    private fun checkCredentials(cred: Credentials) {
        setStateFromCredentials(cred)
    }

    private fun setStateFromCredentials(cred: Credentials) {
        cred.user.isEmailVerified?.let {
            when (it) {
                true -> {
                    authenticationDataWriter.authenticationStateWriter.value = AuthenticationState.Authenticated
                    authenticationDataWriter.userNameWriter.value = cred.user.name ?: ""
                    authenticationDataWriter.roleWriter.value = Role.USER
                }

                false -> {
                    authenticationDataWriter.authenticationStateWriter.value = AuthenticationState.AuthenticatedWaitingEmailConfirmation
                    authenticationDataWriter.userNameWriter.value = cred.user.name ?: ""
                    authenticationDataWriter.roleWriter.value = Role.USER
                }
            }
        }
    }

    override suspend fun initCredentials() {

        if (credentialsManager.hasValidCredentials()) {
            try {
                val cred = credentialsManager.awaitCredentials()
                setStateFromCredentials(cred)
            } catch (error: CredentialsManagerException) {
                credentialsManager.clearCredentials()
            }
        }
    }

    override suspend fun logout(context: Context) {
        WebAuthProvider.logout(auth0).withScheme("agiza").await(context)
        credentialsManager.clearCredentials()
        authenticationDataWriter.authenticationStateWriter.value =
            AuthenticationState.Unauthenticated
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        TODO("Not yet implemented")
    }


}
