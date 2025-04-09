package com.kaaneneskpc.deliverr.ui.features.orders.order_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.deliverr.R
import com.kaaneneskpc.deliverr.ui.features.orders.components.OrderDetailsText
import com.kaaneneskpc.deliverr.util.StringUtils
import kotlinx.coroutines.flow.collectLatest


@Composable
fun OrderDetailsScreen(
    navController: NavController,
    orderID: String,
    viewModel: OrderDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = orderID) {
        viewModel.getOrderDetails(orderID)
    }

    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest {
            when (it) {
                is OrderDetailsViewModel.OrderDetailsEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                modifier = Modifier
                    .shadow(12.dp, clip = true, shape = CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        viewModel.navigateBack()
                    },
                contentDescription = "Back",
            )
            Text(text = "Order Details", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.size(48.dp))
        }
        val uiState = viewModel.state.collectAsStateWithLifecycle()
        when (uiState.value) {
            is OrderDetailsViewModel.OrderDetailsState.Loading -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                    Text(text = "Loading")
                }
            }

            is OrderDetailsViewModel.OrderDetailsState.OrderDetails -> {
                val order =
                    (uiState.value as OrderDetailsViewModel.OrderDetailsState.OrderDetails).order
                OrderDetailsText(order)
                Row {
                    Text(text = "Price:")
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = StringUtils.formatCurrency(order.totalAmount))
                }
                Row {
                    Text(text = "Date:")
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = order.createdAt)
                }
                Row {
                    Image(
                        painter = painterResource(id = viewModel.getImage(order)),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(text = order.status)
                }
            }

            is OrderDetailsViewModel.OrderDetailsState.Error -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = (uiState.value as OrderDetailsViewModel.OrderDetailsState.Error).message)
                    Button(onClick = { viewModel.getOrderDetails(orderID) }) {
                        Text(text = "Retry")
                    }
                }
            }
        }


    }
}