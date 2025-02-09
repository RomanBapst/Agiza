package com.example.agiza.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Product(
    val id: Int,
    val name: String,
    @SerialName("created_at") val created: String,
    val price: Float,
    val owner: String,
    val payload: JsonElement,
    val available: Boolean? = null
)
@Serializable
data class AddProduct(
    val name: String,
    val price: Float,
    val payload: JsonElement?,
    val available: Boolean? = null
)
