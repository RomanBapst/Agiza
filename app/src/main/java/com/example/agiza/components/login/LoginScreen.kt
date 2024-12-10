package com.example.agiza.components.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agiza.components.authentication.AuthenticationState
import kotlinx.serialization.Serializable

@Serializable
object Login

@Composable
fun LoginScreen(modifier: Modifier = Modifier, onNavigateToHome : () -> Unit) {

    val viewmodel : LoginViewModel = viewModel(factory = LoginViewModel.Factory)
    val userName = viewmodel.userName.collectAsState()
    val context = LocalContext.current

    val email = viewmodel.email.collectAsState()
    val password = viewmodel.password.collectAsState()

    LaunchedEffect(key1 = true) {
        viewmodel.uiEvent.collect {
            when(it) {
                is LoginUiEvent.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    val authenticatedState = viewmodel.authenticationState.collectAsState()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        when(authenticatedState.value) {
            AuthenticationState.Unauthenticated -> {
                TextField(value = email.value, onValueChange = {viewmodel.updateEmail(it)}, placeholder = { Text("Email") } )
                TextField(value = password.value, onValueChange = {viewmodel.updatePassword(it)}, placeholder = {Text("Password") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), visualTransformation = PasswordVisualTransformation())
                Button(onClick = { viewmodel.loginWithEmailAndPassword(email.value, password.value)}) {
                    Text(text = "Login")
                }
            }
            AuthenticationState.Authenticating -> {
                CircularProgressIndicator()
            }

            AuthenticationState.AuthenticatedWaitingEmailConfirmation -> {
                Text(text = "Please verify email and then login")
                Button(onClick = { viewmodel.onLoginClicked(context)  }) {
                    Text(text = "Login")
                }
            }
            AuthenticationState.Authenticated -> {
            }
        }
    }
}
