package com.example.agiza.domain.models

import org.maplibre.android.geometry.LatLng

data class AgentModel(
    val id: String,
    val name: String,
    val lastLocation: LatLng
)