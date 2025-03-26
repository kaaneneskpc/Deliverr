package com.kaaneneskpc.deliverr.ui.features.restaurant


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItem
import com.kaaneneskpc.deliverr.data.remote.ApiResponse
import com.kaaneneskpc.deliverr.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RestaurantViewModel @Inject constructor(val foodApi: FoodApi) : ViewModel() {
    var errorMsg = ""
    var errorDescription = ""

    private val _uiState = MutableStateFlow<RestaurantEvent>(RestaurantEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<RestaurantNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun getFoodItem(id: String) {
        viewModelScope.launch {
            _uiState.value = RestaurantEvent.Loading
            try {
                val response = safeApiCall {
                    foodApi.getFoodItemForRestaurant(id)
                }
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.value = RestaurantEvent.Success(response.data.foodItems)
                    }

                    else -> {
                        val error =
                            (response as? ApiResponse.Error)?.code
                        when (error) {
                            401 -> {
                                errorMsg = "Unauthorized"
                                errorDescription = "You are not authorized to view this content"
                            }

                            404 -> {
                                errorMsg = "Not Found"
                                errorDescription = "The Restaurant was not found"
                            }

                            else -> {
                                errorMsg = "Error"
                                errorDescription = "An error occurred"
                            }
                        }
                        _uiState.value = RestaurantEvent.Error
                        _navigationEvent.emit(RestaurantNavigationEvent.ShowErrorDialog)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = RestaurantEvent.Error
                _navigationEvent.emit(RestaurantNavigationEvent.ShowErrorDialog)
            }
        }
    }


    sealed class RestaurantNavigationEvent {
        data object GoBack : RestaurantNavigationEvent()
        data object ShowErrorDialog : RestaurantNavigationEvent()
        data class NavigateToProduceDetails(val productID: String) : RestaurantNavigationEvent()
    }

    sealed class RestaurantEvent {
        data object Nothing : RestaurantEvent()
        data class Success(val foodItems: List<FoodItem>) : RestaurantEvent()
        data object Error : RestaurantEvent()
        data object Loading : RestaurantEvent()
    }
}