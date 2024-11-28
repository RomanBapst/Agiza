package com.example.agiza.components.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.agiza.JetpackComposeApp
import com.example.agiza.Repository
import com.example.agiza.components.login.LoginUiEvent
import com.example.agiza.components.login.LoginViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed class HomeUiEvent {
    data object NavigateToLogin : HomeUiEvent()
}

class HomeViewModel(private val repository: Repository) : ViewModel() {


    val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val role = repository.role
    val shops = repository.shops

    fun logout(context: Context) {
        viewModelScope.launch {
            repository.logout(context)
            _uiEvent.send(HomeUiEvent.NavigateToLogin)
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as JetpackComposeApp)
                val repository = application.container.repository
                HomeViewModel(repository)
            }
        }
    }

}
