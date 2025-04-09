package com.kaaneneskpc.deliverr.ui.features.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.models.response.order.Order
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
class OrderListViewModel @Inject constructor(private val foodApi: FoodApi) : ViewModel() {

    private val _state = MutableStateFlow<OrderListState>(OrderListState.Loading)
    val state get() = _state.asStateFlow()

    private val _event = MutableSharedFlow<OrderListEvent>()
    val event get() = _event.asSharedFlow()

    init {
        getOrders()
    }

    fun navigateToDetails(order: Order) {
        viewModelScope.launch {
            _event.emit(OrderListEvent.NavigateToOrderDetailScreen(order))
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _event.emit(OrderListEvent.NavigateBack)
        }
    }

    fun getOrders() {
        viewModelScope.launch {
            _state.value = OrderListState.Loading
            val result = safeApiCall { foodApi.getOrders() }
            when (result) {
                is ApiResponse.Success -> {
                    _state.value = OrderListState.OrderList(result.data.orders)
                }

                is ApiResponse.Error -> {
                    _state.value = OrderListState.Error(result.message)
                }

                is ApiResponse.Exception -> {
                    _state.value =
                        OrderListState.Error(result.exception.message ?: "An error occurred")
                }
            }
        }
    }

    sealed class OrderListEvent {
        data class NavigateToOrderDetailScreen(val order: Order) : OrderListEvent()
        object NavigateBack : OrderListEvent()
    }

    sealed class OrderListState {
        object Loading : OrderListState()
        data class OrderList(val orderList: List<Order>) : OrderListState()
        data class Error(val message: String) : OrderListState()
    }
}