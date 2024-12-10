package com.example.agiza.components.login

import android.content.Context
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.maplibre.android.utils.ThreadUtils.init

sealed class LoginUiEvent {
    data object NavigateToHome : LoginUiEvent()
}

class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    val userName = repository.userName
    val authenticationState = repository.authenticationState

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun updateEmail(email : String) {
        _email.value = email
    }
    fun updatePassword(password : String) {
        _password.value = password
    }

    init {
        viewModelScope.launch {
            authenticationState.collect {
                if (authenticationState.value == AuthenticationState.Authenticated) {
                    _uiEvent.send(LoginUiEvent.NavigateToHome)
                }
            }
        }

        viewModelScope.launch {
            repository.initCredentials()
        }
    }

    fun onLoginClicked(context: Context) {
        viewModelScope.launch {
            repository.login(context)
        }
    }

    fun loginWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            repository.loginWithEmailAndPassword(email, password)
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