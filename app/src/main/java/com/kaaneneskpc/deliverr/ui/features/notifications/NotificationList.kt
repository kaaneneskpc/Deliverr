package com.kaaneneskpc.deliverr.ui.features.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.deliverr.ui.features.notifications.components.ErrorScreen
import com.kaaneneskpc.deliverr.ui.features.notifications.components.LoadingScreen
import com.kaaneneskpc.deliverr.ui.features.notifications.components.NotificationItem
import com.kaaneneskpc.deliverr.ui.navigation.OrderDetails
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@Composable
fun NotificationsList(navController: NavController, viewModel: NotificationsViewModel) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    
    LaunchedEffect(key1 = Unit) {
        viewModel.getNotifications()
    }
    
    LaunchedEffect(state.value) {
        if (state.value is NotificationsViewModel.NotificationsState.Success) {
            listState.animateScrollToItem(0)
        }
    }
    
    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest {
            when (it) {
                is NotificationsViewModel.NotificationsEvent.NavigateToOrderDetail -> {
                    navController.navigate(OrderDetails(it.orderID))
                }
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {

        when (state.value) {
            is NotificationsViewModel.NotificationsState.Loading -> {
                LoadingScreen()
            }

            is NotificationsViewModel.NotificationsState.Success -> {
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
                val notifications =
                    (state.value as NotificationsViewModel.NotificationsState.Success).data
                LazyColumn(
                    state = listState
                ) {
                    items(notifications, key = { it.id }) {
                        NotificationItem(it) {
                            viewModel.readNotification(it)
                        }
                    }
                }
            }

            is NotificationsViewModel.NotificationsState.Error -> {
                val message =
                    (state.value as NotificationsViewModel.NotificationsState.Error).message
                ErrorScreen(message = message) {
                    viewModel.getNotifications()
                }
            }
        }

    }
}