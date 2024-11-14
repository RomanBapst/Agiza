package com.example.agiza.components.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.example.agiza.Repository
import kotlinx.coroutines.launch


class LoginViewModel(val repository: Repository) : ViewModel() {

    val userName = repository.userName
    val authenticationState = repository.authenticationState

    fun onLoginClicked(context: Context) {
        viewModelScope.launch {
            repository.login(context)
        }
    }

}