package com.example.agiza.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Symbol
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agiza.BuildConfig
import com.example.agiza.R
import com.example.agiza.components.login.LoginUiEvent
import kotlinx.serialization.Serializable
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style
import org.ramani.compose.Circle
import org.ramani.compose.MapLibre
import org.ramani.compose.Symbol

@Serializable
object HomeScreen

@Composable
fun HomeScreen(onNavigateToLogin: () -> Unit) {

    val homeViewModel = viewModel<HomeViewModel>(factory = HomeViewModel.Factory)
    val role = homeViewModel.role.collectAsState()
    val cameraPosition = org.ramani.compose.CameraPosition(
        target = LatLng(latitude = -3.37, longitude = 36.69),
        zoom = 15.0
    )
    val context = LocalContext.current
    val shops = homeViewModel.shops.collectAsState()

    LaunchedEffect(key1 = true) {
        homeViewModel.uiEvent.collect {
            when(it) {
                is HomeUiEvent.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Logged in as ${role.value}")
        Button(onClick = {homeViewModel.logout(context)}) {
            Text("Logout")
        }
        MapLibre( modifier = Modifier.fillMaxSize(), styleBuilder = Style.Builder().fromUri("https://api.maptiler.com/maps/satellite/style.json?key=${BuildConfig.MAPS_API_KEY}"), cameraPosition = cameraPosition ) {
            shops.value.forEach {
                Symbol(imageId = org.maplibre.android.R.drawable.maplibre_marker_icon_default , center = LatLng(it.lat, it.lon), size = 1.0f, color = "Red", isDraggable = false, zIndex = 1, text = it.name)
               // Circle(center = LatLng(it.lat, it.lon), radius = 20.0f, color = "Red", isDraggable = false, zIndex = 1)
            }
        }
    }
}
