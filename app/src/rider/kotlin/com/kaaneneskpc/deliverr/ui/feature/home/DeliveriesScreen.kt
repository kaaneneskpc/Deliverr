package com.kaaneneskpc.deliverr.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.deliverr.ui.features.notifications.components.ErrorScreen
import com.kaaneneskpc.deliverr.ui.features.notifications.components.LoadingScreen
import com.kaaneneskpc.deliverr.util.StringUtils

@Composable
fun DeliveriesScreen(
    navController: NavController,
    homeViewModel: DeliveriesViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Deliveries",
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        val uiState = homeViewModel.deliveriesState.collectAsStateWithLifecycle()
        when (val state = uiState.value) {
            is DeliveriesViewModel.DeliveriesState.Loading -> {
                LoadingScreen()
            }

            is DeliveriesViewModel.DeliveriesState.Success -> {
                LazyColumn {
                    items(state.deliveries) { delivery ->

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = delivery.customerAddress,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = delivery.restaurantAddress,
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = delivery.orderId,
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${delivery.estimatedDistance} km",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = StringUtils.formatCurrency(delivery.estimatedEarning),
                                color = Color.Green
                            )

                            Row {
                                Button(onClick = { homeViewModel.deliveryAccepted(delivery) }) {
                                    Text(text = "Accept")
                                }
                                Button(onClick = { homeViewModel.deliveryRejected(delivery) }) {
                                    Text(text = "Decline")
                                }
                            }
                        }
                    }
                }
            }

            is DeliveriesViewModel.DeliveriesState.Error -> {
                ErrorScreen(message = state.message) {
                    homeViewModel.getDeliveries()
                }
            }
        }
    }
}