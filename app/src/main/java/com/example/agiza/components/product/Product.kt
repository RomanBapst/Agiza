package com.example.agiza.components.product

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Product(
    val name : String
)

interface ProductService {
    val products : StateFlow<List<Product>>
}

class FakeProductsImpl : ProductService {
    override val products = MutableStateFlow(listOf(Product("Gato Negro"), Product(name = "Calvet")))

}