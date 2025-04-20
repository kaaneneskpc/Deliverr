package com.kaaneneskpc.deliverr.ui.feature.order_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.deliverr.ui.feature.order_list.components.OrderListItem
import com.kaaneneskpc.deliverr.ui.features.notifications.components.ErrorScreen
import com.kaaneneskpc.deliverr.ui.features.notifications.components.LoadingScreen
import com.kaaneneskpc.deliverr.ui.navigation.OrderDetails
import kotlinx.coroutines.launch

@Composable
fun OrderListScreen(
    navController: NavController, viewModel: OrdersListViewModel = hiltViewModel()
) {
    val listOfItems = viewModel.getOrderTypes()
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Order List",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium
        )
        val pagerState = rememberPagerState(pageCount = { listOfItems.size })
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(key1 = pagerState.currentPage) {
            viewModel.getOrdersByType(listOfItems[pagerState.currentPage])
        }
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOfItems.forEachIndexed { index, item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        HorizontalPager(state = pagerState) { page ->
            Column {
                val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                when (uiState.value) {
                    is OrdersListViewModel.OrdersScreenState.Loading -> {
                        LoadingScreen()
                    }

                    is OrdersListViewModel.OrdersScreenState.Success -> {
                        val orders =
                            (uiState.value as OrdersListViewModel.OrdersScreenState.Success).data
                        LazyColumn {
                            items(orders) { order ->
                                OrderListItem(order = order) {
                                    navController.navigate(OrderDetails(order.id))
                                }
                            }
                        }
                    }

                    is OrdersListViewModel.OrdersScreenState.Failed -> {
                        ErrorScreen(message = "Failed to load data") {
                            viewModel.getOrdersByType(listOfItems[pagerState.currentPage])
                        }
                    }
                }

            }

        }
    }
}