package com.kaaneneskpc.deliverr.data.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocationManager @Inject constructor(private val fusedLocationProviderClient: FusedLocationProviderClient) {

    @SuppressLint("MissingPermission")
    fun getLocation(): Flow<Location> = flow {
        val location = fusedLocationProviderClient.lastLocation.await()
        emit(location)
    }.flowOn(Dispatchers.IO)

    private val _locationUpdate = MutableStateFlow<Location?>(null)
    val locationUpdate = _locationUpdate.asStateFlow()

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
    var locationCallback: LocationCallback? = null


    @SuppressLint("MissingPermission")
    fun startLocationUpdate() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                _locationUpdate.value = locationResult.lastLocation
            }
        }
        val looper =  Looper.getMainLooper()?: Looper.myLooper()
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                looper
            )
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun stopLocationUpdate() {
        locationCallback?.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
    }
}