package com.kaaneneskpc.deliverr.ui.features.add_address

import android.location.Address
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.location.LocationManager
import com.kaaneneskpc.deliverr.data.models.request.ReverseGeoCodeRequest
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
class AddAddressViewModel @Inject constructor(
    val foodApi: FoodApi,
    private val locationManager: LocationManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddAddressState>(AddAddressState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<AddAddressEvent>()
    val event = _event.asSharedFlow()

    private val _address = MutableStateFlow<com.kaaneneskpc.deliverr.data.models.response.address.Address?>(null)
    val address = _address.asStateFlow()
    fun getLocation() = locationManager.getLocation()

    fun reverseGeocode(lat: Double, lon: Double) {
        viewModelScope.launch {
            _address.value = null
            val address = safeApiCall { foodApi.reverseGeocode(ReverseGeoCodeRequest(lat, lon)) }
            when (address) {
                is ApiResponse.Success -> {
                    _address.value = address.data
                    _uiState.value = AddAddressState.Success
                }

                else -> {
                    _address.value = null
                    _uiState.value = AddAddressState.Error("Failed to reverse geocode")
                }
            }
        }

    }

    fun onAddAddressClicked() {
        viewModelScope.launch {
            _uiState.value = AddAddressState.AddressStoring
            val result = safeApiCall { foodApi.storeAddress(address.value!!) }
            when (result) {
                is ApiResponse.Success -> {
                    _uiState.value = AddAddressState.Success
                    _event.emit(AddAddressEvent.NavigateToAddressList)
                }

                else -> {
                    _uiState.value = AddAddressState.Error("Failed to store address")
                }
            }
        }
    }

    sealed class AddAddressEvent {
        object NavigateToAddressList : AddAddressEvent()
    }

    sealed class AddAddressState {
        object Loading : AddAddressState()
        object Success : AddAddressState()
        object AddressStoring : AddAddressState()
        data class Error(val message: String) : AddAddressState()
    }
}