package com.example.agiza.components.agent.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agiza.libui.CustomTextField
import com.example.agiza.R
import kotlinx.serialization.Serializable

@Serializable
object AddProduct


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct(onNavigateToProducts : () -> Unit) {
    val productViewModel = viewModel<AddProductViewModel>(factory = AddProductViewModel.Factory)

    LaunchedEffect(key1 = true) {
        productViewModel.uiEvent.collect {
            when(it) {
                AddProductUiEvent.NavigateToProducts -> onNavigateToProducts()
            }
        }
    }

    val productName = productViewModel.productName.collectAsState()
    val productPrice = productViewModel.productPrice.collectAsState()



    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            CustomTextField(title = "Name", textState = productName.value, onTextChange = { key, text ->
                productViewModel.nameChanged(text)
            }, keyboardType = KeyboardType.Text)
            CustomTextField(title = "Price", textState = productPrice.value.toString(), onTextChange = { key, text ->
                productViewModel.priceChanged(text)
            }, keyboardType = KeyboardType.Number)
        }
        FloatingActionButton(modifier = Modifier.align(Alignment.BottomEnd),onClick = { productViewModel.addProduct(productName.value, productPrice.value) }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }

    }
}
