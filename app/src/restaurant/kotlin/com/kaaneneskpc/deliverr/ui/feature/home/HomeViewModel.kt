package com.kaaneneskpc.deliverr.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.deliverr.DeliverrSession
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.models.response.restaurant.Restaurant
import com.kaaneneskpc.deliverr.data.remote.ApiResponse
import com.kaaneneskpc.deliverr.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val foodApi: FoodApi, val deliverrSession: DeliverrSession) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getRestaurantProfile()
    }

    fun getRestaurantProfile() {
        viewModelScope.launch {
            _uiState.value = HomeScreenState.Loading
            val response = safeApiCall { foodApi.getRestaurantProfile() }
            when (response) {
                is ApiResponse.Success -> {
                    _uiState.value = HomeScreenState.Success(response.data)
                    deliverrSession.storeRestaurantId(response.data.id)
                }

                is ApiResponse.Error -> {
                    _uiState.value = HomeScreenState.Failed
                }

                is ApiResponse.Exception -> {
                    _uiState.value = HomeScreenState.Failed
                }
            }
        }
    }

    fun retry() {
        getRestaurantProfile()
    }

    sealed class HomeScreenState {
        object Loading : HomeScreenState()
        object Failed : HomeScreenState()
        data class Success(val data: Restaurant) : HomeScreenState()
    }
}
