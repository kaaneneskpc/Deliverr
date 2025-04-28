package com.kaaneneskpc.deliverr.ui.feature.menu.list

import android.widget.Button
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.deliverr.common.FoodItemView
import com.kaaneneskpc.deliverr.ui.features.notifications.components.ErrorScreen
import com.kaaneneskpc.deliverr.ui.features.notifications.components.LoadingScreen
import com.kaaneneskpc.deliverr.ui.navigation.AddMenu
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ListMenuItemsScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: ListMenuItemViewModel = hiltViewModel()
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddItemClicked() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Item"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val uiState = viewModel.listMenuItemState.collectAsStateWithLifecycle()
            LaunchedEffect(key1 = true) {
                viewModel.menuItemEvent.collectLatest {
                    when (it) {
                        is ListMenuItemViewModel.MenuItemEvent.AddNewMenuItem -> {
                            navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("added")
                            navController.navigate(AddMenu)
                        }
                    }
                }
            }
            val isItemAdded =
                navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<Boolean>(
                    "added",
                    false
                )?.collectAsState()
            LaunchedEffect(key1 = isItemAdded?.value) {
                if (isItemAdded?.value == true) {
                    viewModel.retry()
                }
            }

            when (val state = uiState.value) {
                is ListMenuItemViewModel.ListMenuItemState.Loading -> {
                    LoadingScreen()
                }

                is ListMenuItemViewModel.ListMenuItemState.Success -> {
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(state.data, key = { it.id ?: "" }) { item ->
                            FoodItemView(item, animatedVisibilityScope) {
                                //navController.navigate(FoodDetails.route)
                            }
                        }
                    }
                }

                is ListMenuItemViewModel.ListMenuItemState.Error -> {
                    ErrorScreen(message = state.message) {
                        viewModel.retry()
                    }
                }
            }
        }
    }
}