package com.example.agiza.components.product

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.agiza.R
import com.example.agiza.domain.models.ProductModel
import com.example.agiza.domain.usecases.AgentWithProducts

@Composable
fun ExpandableProducts(
    agentWithProducts: AgentWithProducts,
    expanded: Boolean,
    onExpandPressed: () -> Unit,
) {
    ExpandableCard(
        label = agentWithProducts.agent.name,
        expanded = expanded,
        onExpandPressed = onExpandPressed,
        summaryContent = {
        }) {

        ProductList(
            products = agentWithProducts.products,
        )
    }
}


@Composable
fun ProductList(
    products: List<ProductModel>,
) {

    LazyColumn(
        modifier = Modifier
            .heightIn(max = 500.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = products) { product ->
            key(product.id) {
                    ProductItem(
                        record = product,
                    )
                }
            Divider(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))
        }
    }
}

@Composable
fun ProductItem(
    record: ProductModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.5f)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                ) {
                    Text(
                        text = record.name,
                        modifier = Modifier.fillMaxSize(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = androidx.compose.material3.MaterialTheme.typography.labelMedium.fontSize
                    )
                }

            }
        }
        Surface(
            modifier = Modifier.weight(0.6f)
        ) {

            PriceModifier(
                record.id.toInt(),
                record.price.toString(),
                increasePay = {},
                decreasePay = {},
                edited = { i: Int, s: String -> },
                onEditingFinished = { i: Int, s: String -> },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun PriceModifier(
    id: Int,
    salary: String,
    increasePay: (Int) -> Unit,
    decreasePay: (Int) -> Unit,
    edited: (Int, String) -> Unit,
    onEditingFinished: (Int, String) -> Unit,
    modifier: Modifier
) {
}

@Composable
fun ExpandableCard(
    label: String,
    expanded: Boolean,
    onExpandPressed: () -> Unit,
    summaryContent: @Composable () -> Unit,
    expandableContent: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        border = BorderStroke(2.dp, Color.Black),
    ) {
        Column() {
            androidx.compose.material3.Card(
                modifier = Modifier.padding(all = 5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 30.dp)
            ) {
                ExpandableCardSummary(
                    label = label,
                    expanded = expanded,
                ) {
                    summaryContent.invoke()
                }
            }
            ExpandableContent(
                expanded
            ) {
                expandableContent.invoke()
            }
        }
    }
}
@Composable
fun ExpandableCardSummary(
    label: String,
    expanded: Boolean,
    lowerContent: @Composable () -> Unit,
) {
    Card() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(10.dp),
        ) {

                SummaryHeader(
                    label = label,
                    expanded = expanded,
                    onLabelClicked = {},
                    onAddClicked = {},
                    onExpandPressed = {},
                )
            lowerContent.invoke()

        }
    }
}
@Composable
fun SummaryHeader(
    label: String,
    expanded: Boolean,
    onLabelClicked: (String) -> Unit,
    onAddClicked: (String) -> Unit,
    onExpandPressed: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onLabelClicked(label) },
            modifier = Modifier.weight(0.6f)
        ) {
            Text(
                text = label,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                textAlign = TextAlign.Center,
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.gear),
            contentDescription = "add_employees_to_work",
            modifier = Modifier
                .clickable { onAddClicked(label) }
                .weight(0.2f)
        )

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "Expand Arrow",
            modifier = Modifier
                .size(50.dp)
                .rotate(
                    if (expanded) 180.0f else 0.0f
                )
                .clickable {
                    onExpandPressed()
                }
                .weight(0.2f)
        )
    }
}

@Composable
fun ExpandableContent(
    expanded: Boolean,
    content: @Composable () -> Unit,
) {
    val enterTransiton = remember {
        expandVertically(expandFrom = Alignment.Top, animationSpec = tween(1000)) +
                fadeIn(
                    initialAlpha = 0.3f, animationSpec = tween(1000)
                )
    }

    val exitTransition = remember {
        shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(1000)) +
                fadeOut(animationSpec = tween(1000))
    }
    AnimatedVisibility(visible = expanded, enter = enterTransiton, exit = exitTransition) {
        content.invoke()
    }

}

