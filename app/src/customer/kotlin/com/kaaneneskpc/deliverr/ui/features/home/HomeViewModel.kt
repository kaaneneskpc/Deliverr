package com.kaaneneskpc.deliverr.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.models.response.home.Category
import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItem
import com.kaaneneskpc.deliverr.data.models.response.restaurant.Restaurant
import com.kaaneneskpc.deliverr.data.remote.ApiResponse
import com.kaaneneskpc.deliverr.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val foodApi: FoodApi) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<HomeScreenNavigationEvents?>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    var categories = emptyList<Category>()
    var restaurants = emptyList<Restaurant>()
    var popularFoodItems = emptyList<FoodItem>()

    init {
        viewModelScope.launch {
            categories = getCategories()
            restaurants = getPopularRestaurants()
            popularFoodItems = getPopularFoodItems()

            if (categories.isNotEmpty() || restaurants.isNotEmpty() || popularFoodItems.isNotEmpty()) {
                _uiState.value = HomeScreenState.Success
            } else {
                _uiState.value = HomeScreenState.Empty
            }
        }
    }

    private suspend fun getCategories(): List<Category> {

        var list = emptyList<Category>()
        val response = safeApiCall {
            foodApi.getCategories()
        }
        when (response) {
            is ApiResponse.Success -> {
                list = response.data.data
            }

            else -> {
            }
        }
        return list
    }

    private suspend fun getPopularRestaurants(): List<Restaurant> {
        var list = emptyList<Restaurant>()
        val response = safeApiCall {
            foodApi.getRestaurants(40.7128, -74.0060)
        }
        when (response) {
            is ApiResponse.Success -> {
                list = response.data.data

            }

            else -> {
            }
        }
        return list
    }

    private suspend fun getPopularFoodItems(): List<FoodItem> {
        val foodItems: List<FoodItem>

        val restaurantsList = getPopularRestaurants()

        val topRestaurants = restaurantsList.take(3)

        val tempList = mutableListOf<FoodItem>()

        topRestaurants.forEach { restaurant ->
            val response = safeApiCall {
                foodApi.getFoodItemForRestaurant(restaurant.id)
            }

            when (response) {
                is ApiResponse.Success -> {
                    val restaurantItems = response.data.foodItems.take(2)
                    tempList.addAll(restaurantItems)
                }

                else -> {}
            }
        }

        foodItems = tempList
        return foodItems
    }

    fun onRestaurantSelected(it: Restaurant) {
        viewModelScope.launch {
            _navigationEvent.emit(
                HomeScreenNavigationEvents.NavigateToDetail(
                    it.name,
                    it.imageUrl,
                    it.id
                )
            )
        }
    }

    fun onFoodItemSelected(foodItem: FoodItem) {
        viewModelScope.launch {
            foodItem.restaurantId.let { restaurantId ->
                val selectedRestaurant = restaurants.find { it.id == restaurantId }
                selectedRestaurant?.let {
                    _navigationEvent.emit(
                        HomeScreenNavigationEvents.NavigateToDetail(
                            it.name,
                            it.imageUrl,
                            it.id
                        )
                    )
                }
            }
        }
    }

    sealed class HomeScreenState {
        object Loading : HomeScreenState()
        object Empty : HomeScreenState()
        object Success : HomeScreenState()
    }

    sealed class HomeScreenNavigationEvents {
        data class NavigateToDetail(val name: String, val imageUrl: String, val id: String) :
            HomeScreenNavigationEvents()
    }
}