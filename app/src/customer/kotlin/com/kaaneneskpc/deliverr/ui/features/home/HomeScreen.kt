package com.kaaneneskpc.deliverr.ui.features.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.deliverr.data.models.response.home.Category
import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItem
import com.kaaneneskpc.deliverr.data.models.response.restaurant.Restaurant
import com.kaaneneskpc.deliverr.ui.features.home.components.CategoryItem
import com.kaaneneskpc.deliverr.ui.features.home.components.PopularFoodItem
import com.kaaneneskpc.deliverr.ui.features.home.components.RestaurantItem
import com.kaaneneskpc.deliverr.ui.navigation.RestaurantDetails
import com.kaaneneskpc.deliverr.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest {
            when (it) {
                is HomeViewModel.HomeScreenNavigationEvents.NavigateToDetail -> {
                    navController.navigate(RestaurantDetails(it.id, it.name, it.imageUrl))
                }

                else -> {

                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        when (uiState.value) {
            is HomeViewModel.HomeScreenState.Loading -> {
                Text(text = "Loading")
            }

            is HomeViewModel.HomeScreenState.Empty -> {
                Text(text = "Empty")
            }

            is HomeViewModel.HomeScreenState.Success -> {
                CategoriesList(categories = viewModel.categories, onCategorySelected = {})

                PopularFoodList(
                    foodItems = viewModel.popularFoodItems,
                    onFoodItemSelected = { viewModel.onFoodItemSelected(it) }
                )

                RestaurantList(
                    restaurants = viewModel.restaurants,
                    animatedVisibilityScope,
                    onRestaurantSelected = {
                        viewModel.onRestaurantSelected(it)
                    })
            }
        }
    }
}

@Composable
fun CategoriesList(categories: List<Category>, onCategorySelected: (Category) -> Unit) {
    LazyRow {
        items(categories) {
            CategoryItem(category = it, onCategorySelected = onCategorySelected)
        }
    }
}

@Composable
fun PopularFoodList(foodItems: List<FoodItem>, onFoodItemSelected: (FoodItem) -> Unit) {
    if (foodItems.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Popular Food Nearby",
                    style = Typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = { }) {
                    Text(text = "View All", style = Typography.bodySmall)
                }
            }

            LazyRow {
                items(foodItems) { foodItem ->
                    PopularFoodItem(
                        foodItem = foodItem,
                        onFoodItemSelected = onFoodItemSelected
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantList(
    restaurants: List<Restaurant>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onRestaurantSelected: (Restaurant) -> Unit
) {
    Column {
        Row {
            Text(
                text = "Popular Restaurants",
                style = Typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { }) {
                Text(text = "View All", style = Typography.bodySmall)
            }
        }
    }
    LazyRow {
        items(restaurants) {
            RestaurantItem(it, animatedVisibilityScope, onRestaurantSelected)
        }
    }
}