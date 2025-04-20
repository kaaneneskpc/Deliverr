package com.kaaneneskpc.deliverr.ui.features.order_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kaaneneskpc.deliverr.data.models.response.order.Order

@Composable
fun OrderDetailsText(order: Order) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            AsyncImage(
                model = order.restaurant.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(
                    text = order.id,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
                Text(text = "${order.items.size} items", color = Color.Gray)
                Text(
                    text = order.restaurant.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }
        }
        Text(text = "Status", color = Color.Gray)
        Text(text = order.status, color = Color.Black)
        Spacer(modifier = Modifier.size(16.dp))
    }
}