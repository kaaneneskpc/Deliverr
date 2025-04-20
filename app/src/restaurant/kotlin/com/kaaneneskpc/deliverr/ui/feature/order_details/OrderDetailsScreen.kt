package com.kaaneneskpc.deliverr.ui.feature.order_details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.deliverr.ui.features.notifications.components.ErrorScreen
import com.kaaneneskpc.deliverr.ui.features.notifications.components.LoadingScreen
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrderDetailsScreen(
    orderID: String,
    navController: NavController,
    viewModel: OrderDetailsViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = orderID) {
        viewModel.getOrderDetails(orderID)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Order Details")
        LaunchedEffect(key1 = true) {
            viewModel.event.collectLatest {
                when (it) {
                    is OrderDetailsViewModel.OrderDetailsEvent.NavigateBack -> {
                        navController.popBackStack()
                    }

                    is OrderDetailsViewModel.OrderDetailsEvent.ShowPopUp -> {
                        Toast.makeText(navController.context, it.msg, Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                    }
                }
            }
        }

        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        when (uiState.value) {
            is OrderDetailsViewModel.OrderDetailsUiState.Loading -> {
                LoadingScreen()
            }

            is OrderDetailsViewModel.OrderDetailsUiState.Error -> {
                ErrorScreen(message = "Something went wrong") {
                    viewModel.getOrderDetails(orderID)
                }
            }

            is OrderDetailsViewModel.OrderDetailsUiState.Success -> {
                val order =
                    (uiState.value as OrderDetailsViewModel.OrderDetailsUiState.Success).order
                Text(text = order.id)
                Spacer(modifier = Modifier.padding(8.dp))
                order.items.forEach {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp)
                            .background(Color.White).padding(16.dp)
                    ) {
                        Text(text = it.menuItemName ?: "")
                        Text(text = it.quantity.toString())
                    }
                }
                FlowRow(modifier = Modifier.fillMaxWidth()) {
                    viewModel.listOfStatus.forEach {
                        Button(
                            onClick = { viewModel.updateOrderStatus(orderID, it) },
                            enabled = order.status != it
                        ) {
                            Text(text = it)
                        }
                    }
                }
            }
        }


    }
}