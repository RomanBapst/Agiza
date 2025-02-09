package com.example.agiza.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Agent(
    val id: String,
    val name: String,
    @SerialName("created_at") val created : String,
    @SerialName("last_lat") val lastLatitude : Double?,
    @SerialName("last_lon") val lastLongitude: Double?
)
