package com.kaaneneskpc.deliverr.ui.features.order_success

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.kaaneneskpc.deliverr.ui.navigation.Home

@Composable
fun OrderSuccess(orderID: String, navController: NavController) {
    BackHandler {
        navController.popBackStack(route = Home, inclusive = false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(text = "Order Success", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Order ID: $orderID",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Button(onClick = {
            navController.popBackStack(route = Home, inclusive = false)
        }) {
            Text(text = "Continue Shopping")
        }
    }
}