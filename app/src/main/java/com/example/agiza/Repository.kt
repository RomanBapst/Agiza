package com.example.agiza

import android.content.Context
import com.example.agiza.components.authentication.AuthenticationService
import com.example.agiza.components.product.ProductService
import com.example.agiza.components.shops.ShopsService


class Repository(private val authenticator : AuthenticationService, private val shopsService : ShopsService, private val productsService: ProductService) {
    val userName = authenticator.authenticationData.userName
    val authenticationState = authenticator.authenticationData.authenticationState
    val role = authenticator.authenticationData.role

    val shops = shopsService.shops
    val products = productsService.products

    suspend fun login(ctx: Context) {
        authenticator.authenticate(ctx)
    }

    suspend fun logout(context: Context) {
        authenticator.logout(context)
    }

    suspend fun initCredentials() {
        authenticator.initCredentials()
    }
}