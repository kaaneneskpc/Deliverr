package com.kaaneneskpc.deliverr.ui.feature.orders.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.models.response.deliveries.DeliveryOrder
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
class OrdersListViewModel @Inject constructor(private val foodApi: FoodApi) : ViewModel() {

    private val _state = MutableStateFlow<OrdersListState>(OrdersListState.Loading)
    val state get() = _state.asStateFlow()

    private val _event = MutableSharedFlow<OrdersListEvent?>()
    val event get() = _event.asSharedFlow()

    init {
        getOrders()
    }

    fun getOrders() {
        viewModelScope.launch {
            _state.value = OrdersListState.Loading
            val response = safeApiCall { foodApi.getActiveDeliveries() }
            when (response) {
                is ApiResponse.Success -> {
                    if(response.data.data.isEmpty()) {
                        _state.value = OrdersListState.Empty
                        return@launch
                    }
                    _state.value = OrdersListState.Success(response.data.data)
                }

                is ApiResponse.Error -> {
                    _state.value = OrdersListState.Error(response.message)
                }

                else -> {
                    _state.value = OrdersListState.Error("Something went wrong")
                }
            }
        }
    }

    sealed class OrdersListEvent {
        object NavigateToOrderDetails : OrdersListEvent()
    }

    sealed class OrdersListState {
        object Loading : OrdersListState()
        object Empty : OrdersListState()
        data class Success(val orders: List<DeliveryOrder>) : OrdersListState()
        data class Error(val message: String) : OrdersListState()
    }
}