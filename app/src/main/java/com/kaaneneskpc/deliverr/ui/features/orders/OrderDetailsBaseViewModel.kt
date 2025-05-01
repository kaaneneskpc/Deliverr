package com.kaaneneskpc.deliverr.ui.features.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.maps.android.PolyUtil
import com.kaaneneskpc.deliverr.data.models.response.socket.SocketLocation
import com.kaaneneskpc.deliverr.data.models.response.socket.SocketLocationResponse
import com.kaaneneskpc.deliverr.data.socket.repository.LocationUpdateBaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

abstract class OrderDetailsBaseViewModel (val repository: LocationUpdateBaseRepository) :
    ViewModel() {
    private val _locationUpdate = MutableStateFlow<SocketLocation?>(null)
    val locationUpdate = _locationUpdate.asStateFlow()

    init {
        processLocation()
    }

    private fun processLocation() {
        viewModelScope.launch {
            repository.messages.collectLatest {
                if (it.isEmpty())
                    return@collectLatest
                val response = Json.decodeFromString(SocketLocationResponse.serializer(), it)
                _locationUpdate.value = SocketLocation(
                    response.currentLocation,
                    response.deliveryPhase,
                    response.estimatedTime,
                    response.finalDestination,
                    response.nextStop,
                    PolyUtil.decode(response.polyline)
                )
            }
        }
    }

    protected fun connectSocket(orderID: String, riderId: String) {
        repository.connect(orderID, riderId)
    }

    protected fun disconnectSocket() {
        repository.disconnect()
    }

}