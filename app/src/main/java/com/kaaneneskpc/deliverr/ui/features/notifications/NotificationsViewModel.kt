package com.kaaneneskpc.deliverr.ui.features.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.models.response.notification.Notification
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
class NotificationsViewModel @Inject constructor(private val foodApi: FoodApi) : ViewModel() {

    private val _state = MutableStateFlow<NotificationsState>(NotificationsState.Loading)
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NotificationsEvent>()
    val event = _event.asSharedFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount = _unreadCount.asStateFlow()

    init {
        getNotifications()
    }

    fun navigateToOrderDetail(orderID: String) {
        viewModelScope.launch {
            _event.emit(NotificationsEvent.NavigateToOrderDetail(orderID))
        }
    }

    fun readNotification(notification: Notification) {
        viewModelScope.launch {
            navigateToOrderDetail(notification.orderId)
            val response = safeApiCall { foodApi.readNotification(notification.id) }
            if (response is ApiResponse.Success) {
                getNotifications()
            }
        }
    }

    fun getNotifications() {
        viewModelScope.launch {
            val response = safeApiCall { foodApi.getNotifications() }
            if (response is ApiResponse.Success) {
                _unreadCount.value = response.data.unreadCount
                _state.value = NotificationsState.Success(response.data.notifications)
            } else {
                _state.value = NotificationsState.Error("Failed to get notifications")
            }
        }
    }

    sealed class NotificationsEvent {
        data class NavigateToOrderDetail(val orderID: String) : NotificationsEvent()
    }

    sealed class NotificationsState {
        object Loading : NotificationsState()
        data class Success(val data: List<Notification>) : NotificationsState()
        data class Error(val message: String) : NotificationsState()
    }
}