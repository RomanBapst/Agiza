package com.example.agiza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.agiza.components.authentication.AuthenticationState
import com.example.agiza.components.login.LoginViewModel
import com.example.agiza.ui.theme.AgizaTheme


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgizaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val viemodel = viewModel<LoginViewModel>()
    val userName = viemodel.userName.collectAsState()
    val context = LocalContext.current

    val authenticatedState = viemodel.authenticationState.collectAsState()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment =Alignment.CenterHorizontally) {

        when(authenticatedState.value) {
            AuthenticationState.Unauthenticated -> {
                Button(onClick = { viemodel.onLoginClicked(context)  }) {
                    Text(text = "Login")
                }
            }
            AuthenticationState.Authenticating -> {
                CircularProgressIndicator()
            }
            AuthenticationState.Authenticated -> {
                Text("Hello " + userName.value)
            }
        }
    }
}