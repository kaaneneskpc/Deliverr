package com.kaaneneskpc.deliverr.ui.features.orders.order_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kaaneneskpc.deliverr.R
import com.kaaneneskpc.deliverr.data.models.response.order.Order
import com.kaaneneskpc.deliverr.data.models.response.order.OrderItem
import com.kaaneneskpc.deliverr.ui.theme.Orange
import com.kaaneneskpc.deliverr.ui.theme.Mustard
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopBar()
            val uiState = viewModel.state.collectAsStateWithLifecycle()
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                when (uiState.value) {
                    is OrderDetailsViewModel.OrderDetailsState.Loading -> {
                        LoadingState()
                    }
                    is OrderDetailsViewModel.OrderDetailsState.OrderDetails -> {
                        val order = (uiState.value as OrderDetailsViewModel.OrderDetailsState.OrderDetails).order
                        OrderDetailsContent(order = order, statusIcon = viewModel.getImage(order))
                    }
                    is OrderDetailsViewModel.OrderDetailsState.Error -> {
                        ErrorState(
                            errorMessage = (uiState.value as OrderDetailsViewModel.OrderDetailsState.Error).message,
                            onRetry = { viewModel.getOrderDetails(orderID) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(viewModel: OrderDetailsViewModel = hiltViewModel()) {
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
        
        Text(
            text = "Order Details",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun LoadingState() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading order details...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ErrorState(errorMessage: String, onRetry: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_pending),
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(72.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Oops! Something went wrong",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp)
        ) {
            Text(
                text = "Retry",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun OrderDetailsContent(order: Order, statusIcon: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = order.restaurant.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.size(16.dp))
                    
                    Column {
                        Text(
                            text = order.restaurant.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "Order #${order.id.takeLast(8).uppercase()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        OrderStatusChip(status = order.status, statusIcon = statusIcon)
                    }
                }
            }
        }
        
        // Order Details Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                SectionTitle(title = "Order Information")
                
                Spacer(modifier = Modifier.height(8.dp))
                
                InfoRow(label = "Date", value = formatDate(order.createdAt))
                InfoRow(label = "Items", value = "${order.items.size} items")
                InfoRow(label = "Payment", value = order.paymentStatus, valueColor = if (order.paymentStatus == "Paid") Color.Green else Color.Red)
                InfoRow(label = "Total", value = StringUtils.formatCurrency(order.totalAmount), isLast = true, valueColor = MaterialTheme.colorScheme.primary)
            }
        }
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                SectionTitle(title = "Delivery Address")
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = order.address.addressLine1,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = order.address.city,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = order.address.zipCode,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                SectionTitle(title = "Order Items")
                
                Spacer(modifier = Modifier.height(8.dp))
                
                order.items.forEachIndexed { index, item ->
                    OrderItemRow(item = item, index = index + 1)
                    
                    if (index < order.items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    isLast: Boolean = false,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isLast) FontWeight.Bold else FontWeight.Medium,
            color = valueColor
        )
    }
}

@Composable
private fun OrderStatusChip(status: String, statusIcon: Int) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                when (status) {
                    "Delivered" -> Color.Green.copy(alpha = 0.1f)
                    "Preparing" -> Mustard.copy(alpha = 0.1f)
                    "On the way" -> Orange.copy(alpha = 0.1f)
                    else -> Color.Gray.copy(alpha = 0.1f)
                }
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = statusIcon),
            contentDescription = status,
            modifier = Modifier.size(16.dp)
        )
        
        Spacer(modifier = Modifier.size(4.dp))
        
        Text(
            text = status,
            style = MaterialTheme.typography.bodySmall,
            color = when (status) {
                "Delivered" -> Color.Green
                "Preparing" -> Mustard
                "On the way" -> Orange
                else -> Color.Gray
            },
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun OrderItemRow(item: OrderItem, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$index",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.size(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Item ID: ${item.menuItemId.takeLast(6).uppercase()}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "Quantity: ${item.quantity}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val parts = dateString.split("T")
        if (parts.size > 1) {
            val datePart = parts[0]
            val timePart = parts[1].split(".")[0]
            "$datePart at $timePart"
        } else {
            dateString
        }
    } catch (e: Exception) {
        dateString
    }
}