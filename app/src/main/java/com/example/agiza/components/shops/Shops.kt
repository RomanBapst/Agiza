package com.example.agiza.components.shops

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.maplibre.android.geometry.LatLng

data class Shop(val name : String, val location : LatLng)

interface ShopsService {
    val shops : StateFlow<List<Shop>>
}

class ShopsImpl : ShopsService {
    override val shops = MutableStateFlow(listOf(Shop(name = "Shoppers", location = LatLng(-3.37, 36.69))))

}