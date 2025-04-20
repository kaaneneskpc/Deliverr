package com.kaaneneskpc.deliverr.ui.features.cart.components

import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kaaneneskpc.deliverr.data.models.response.add_to_cart.CartItem
import com.kaaneneskpc.deliverr.ui.features.food_item.components.FoodItemCounter

@Composable
fun CartItemView(
    cartItem: CartItem,
    onIncrement: (CartItem, Int) -> Unit,
    onDecrement: (CartItem, Int) -> Unit,
    onRemove: (CartItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = cartItem.menuItemId.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(82.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = cartItem.menuItemId.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onRemove.invoke(cartItem) }, modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            Text(
                text = cartItem.menuItemId.description,
                maxLines = 1,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.size(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$${cartItem.menuItemId.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                FoodItemCounter(count = cartItem.quantity,
                    onCounterIncrement = { onIncrement.invoke(cartItem, cartItem.quantity) },
                    onCounterDecrement = { onDecrement.invoke(cartItem, cartItem.quantity) })
            }
        }
    }
}