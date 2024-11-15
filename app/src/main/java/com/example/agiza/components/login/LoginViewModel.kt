package com.example.agiza.components.login

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.agiza.JetpackComposeApp
import com.example.agiza.Repository
import com.example.agiza.components.authentication.AuthenticationState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed class LoginUiEvent {
    class NavigateToHome() : LoginUiEvent()
}

class LoginViewModel(private val repository: Repository) : ViewModel() {

    val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    val userName = repository.userName
    val authenticationState = repository.authenticationState

    init {
        viewModelScope.launch {
            authenticationState.collect {
                println("collecting")
                if (authenticationState.value == AuthenticationState.Authenticated) {
                    _uiEvent.send(LoginUiEvent.NavigateToHome())
                    println("sent crap")
                }
            }
        }
    }

    fun onLoginClicked(context: Context) {
        viewModelScope.launch {
            repository.login(context)
        }
    }
    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as JetpackComposeApp)
                val repository = application.container.repository
                LoginViewModel(repository)
            }
        }
    }
}