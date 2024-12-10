package com.example.agiza.components.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agiza.components.home.HomeViewModel
import kotlinx.serialization.Serializable

@Serializable
object ProductScreen


@Composable
fun ProductScreen() {
    val productViewModel = viewModel<ProductViewModel>(factory = ProductViewModel.Factory)
    val agentsWithProducts = productViewModel.agentWithProducts.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
       agentsWithProducts.value.forEach { agentWithProducts ->
           ExpandableProducts(agentWithProducts, true, {})

       }
    }
}