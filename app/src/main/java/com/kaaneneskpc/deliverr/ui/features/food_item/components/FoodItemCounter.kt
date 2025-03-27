package com.kaaneneskpc.deliverr.ui.features.food_item.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kaaneneskpc.deliverr.R

@Composable
fun FoodItemCounter(onCounterIncrement: () -> Unit, onCounterDecrement: () -> Unit, count: Int) {
    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Image(painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onCounterIncrement.invoke() })
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "$count", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.size(8.dp))
        Image(painter = painterResource(id = R.drawable.ic_minus),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onCounterDecrement.invoke() })

    }
}