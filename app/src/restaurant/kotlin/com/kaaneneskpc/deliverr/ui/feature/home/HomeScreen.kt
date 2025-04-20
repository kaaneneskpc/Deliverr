package com.kaaneneskpc.deliverr.ui.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kaaneneskpc.deliverr.ui.features.notifications.components.ErrorScreen
import com.kaaneneskpc.deliverr.ui.features.notifications.components.LoadingScreen

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (uiState.value) {

            is HomeViewModel.HomeScreenState.Loading -> {
                LoadingScreen()
            }

            is HomeViewModel.HomeScreenState.Success -> {
                val restaurant = (uiState.value as HomeViewModel.HomeScreenState.Success).data
                AsyncImage(
                    model = restaurant.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = restaurant.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = restaurant.address, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = restaurant.createdAt, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }

            is HomeViewModel.HomeScreenState.Failed -> {
                ErrorScreen(message = "Failed to load data") {
                    viewModel.retry()
                }
            }
        }
    }

}