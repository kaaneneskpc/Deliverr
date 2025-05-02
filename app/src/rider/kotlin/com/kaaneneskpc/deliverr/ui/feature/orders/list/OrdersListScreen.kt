package com.kaaneneskpc.deliverr.ui.feature.orders.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.deliverr.data.models.response.deliveries.DeliveryOrder
import com.kaaneneskpc.deliverr.ui.features.notifications.components.ErrorScreen
import com.kaaneneskpc.deliverr.ui.features.notifications.components.LoadingScreen
import com.kaaneneskpc.deliverr.ui.navigation.OrderDetails
import com.kaaneneskpc.deliverr.ui.theme.Primary
import com.kaaneneskpc.deliverr.util.DateUtils
import com.kaaneneskpc.deliverr.util.StringUtils
import com.kaaneneskpc.deliverr.utils.OrdersUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersListScreen(
    navController: NavController,
    viewModel: OrdersListViewModel = hiltViewModel()
) {
    val orderStatuses = remember {
        OrdersUtils.OrderStatus.entries
            .filter { it.name in listOf("ASSIGNED", "OUT_FOR_DELIVERY", "DELIVERED") }
            .map { it.name }
    }
    
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { orderStatuses.size })
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F8F8)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "My Deliveries",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                )
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 16.dp,
                    containerColor = Color.White,
                    contentColor = Primary,
                    indicator = { tabPositions ->
                        if (tabPositions.isNotEmpty() && pagerState.currentPage < tabPositions.size) {
                            SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                height = 3.dp,
                                color = Primary
                            )
                        }
                    },
                    divider = {}
                ) {
                    orderStatuses.forEachIndexed { index, status ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    text = formatOrderStatus(status),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
                                    textAlign = TextAlign.Center,
                                    fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Visible
                                )
                            },
                            selectedContentColor = Primary,
                            unselectedContentColor = Color.Gray
                        )
                    }
                }
                
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val state = viewModel.state.collectAsStateWithLifecycle()
                    
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        when (val currentState = state.value) {
                            is OrdersListViewModel.OrdersListState.Loading -> {
                                LoadingScreen()
                            }
                            
                            is OrdersListViewModel.OrdersListState.Success -> {
                                val filteredOrders = currentState.orders.filter { 
                                    it.status == orderStatuses[page]
                                }
                                
                                if (filteredOrders.isEmpty()) {
                                    EmptyOrdersView(orderStatus = orderStatuses[page])
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        items(filteredOrders) { delivery ->
                                            DeliveryOrderCard(delivery) {
                                                navController.navigate(OrderDetails(delivery.orderId))
                                            }
                                        }
                                        
                                        item {
                                            Spacer(modifier = Modifier.height(80.dp))
                                        }
                                    }
                                }
                            }
                            
                            is OrdersListViewModel.OrdersListState.Empty -> {
                                EmptyOrdersView(orderStatus = orderStatuses[page])
                            }
                            
                            is OrdersListViewModel.OrdersListState.Error -> {
                                ErrorScreen(currentState.message) {
                                    viewModel.getOrders()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryOrderCard(order: DeliveryOrder, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with Order ID and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = order.restaurant.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "Order #${order.orderId.takeLast(8)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                
                StatusChip(status = order.status)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(16.dp))
            
            InfoRow(
                icon = Icons.Default.Create,
                label = "Order Time",
                value = DateUtils.formatISODate(order.createdAt)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            InfoRow(
                icon = Icons.Default.LocationOn,
                label = "Customer Address",
                value = "${order.customer.addressLine1}, ${order.customer.city}"
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            InfoRow(
                icon = Icons.Default.Star,
                label = "Earning",
                value = StringUtils.formatCurrency(order.estimatedEarning),
                valueColor = Color(0xFF4CAF50)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Items summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${order.items.size} item${if (order.items.size > 1) "s" else ""} · ${StringUtils.formatCurrency(order.totalAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (backgroundColor, textColor) = when (status) {
        OrdersUtils.OrderStatus.ASSIGNED.name -> Pair(Color(0xFFE0F7FA), Color(0xFF00838F))
        OrdersUtils.OrderStatus.OUT_FOR_DELIVERY.name -> Pair(Color(0xFFF3E5F5), Color(0xFF6A1B9A))
        OrdersUtils.OrderStatus.DELIVERED.name -> Pair(Color(0xFFE8F5E9), Color(0xFF1B5E20))
        OrdersUtils.OrderStatus.REJECTED.name -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
        OrdersUtils.OrderStatus.CANCELLED.name -> Pair(Color(0xFFEFEBE9), Color(0xFF4E342E))
        else -> Pair(Color(0xFFEEEEEE), Color(0xFF424242))
    }
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = formatOrderStatus(status),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = Color.Black
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(18.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = valueColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EmptyOrdersView(orderStatus: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(64.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "No ${formatOrderStatus(orderStatus)} Deliveries",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "There are currently no deliveries with ${formatOrderStatus(orderStatus).lowercase()} status.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun formatOrderStatus(status: String): String {
    return status.replace("_", " ").lowercase()
        .split(" ")
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}