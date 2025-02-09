package com.example.agiza.components.agent.product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agiza.libui.ExpandableCard
import com.agiza.libui.TwoItemRow
import com.example.agiza.components.home.HomeUiEvent
import com.example.agiza.data.agent.ProductRefreshState
import kotlinx.serialization.Serializable

@Serializable
object ProductScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(onNavigateToAddProduct : () -> Unit) {
    val productViewModel = viewModel<ProductViewModel>(factory = ProductViewModel.Factory)
    productViewModel.agentWithProducts.collectAsState()

    val products = productViewModel.products.collectAsState()

    val productRefreshState = productViewModel.productRefreshState.collectAsState()

    LaunchedEffect(key1 = true) {
        productViewModel.uiEvent.collect {
            when(it) {
                ProductUiEvent.NavigateAddProduct -> onNavigateToAddProduct()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        PullToRefreshBox(
            isRefreshing = productRefreshState.value == ProductRefreshState.REFRESHING,
            onRefresh = { productViewModel.refreshProducts() }) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(products.value) { product ->

                    ExpandableCard(
                        product.name,
                        true,
                        onExpandPressed = {},
                        onEdit = {},
                        onDelete = {},
                        onLabelClicked = {},
                        onAddClicked = {},
                        summaryContent = {}) {
                        Column {
                            TwoItemRow(firstItem = { Text("Price") }) { Text(product.price.toString()) }
                        }
                    }

                }
            }
        }

        FloatingActionButton(onClick = {productViewModel.addProductClicked()}, modifier = Modifier.align(Alignment.BottomEnd).padding(5.dp)) { }
    }
}

@Composable
fun Simple() {
    Text("hello")
}

@Preview
@Composable
fun ProductItemPreview() {
    Simple()
}