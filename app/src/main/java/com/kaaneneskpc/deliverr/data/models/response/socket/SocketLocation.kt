package com.kaaneneskpc.deliverr.data.models.response.socket

import com.google.android.gms.maps.model.LatLng

data class SocketLocation(
    val currentLocation: CurrentLocation,
    val deliveryPhase: String,
    val estimatedTime: Int,
    val finalDestination: FinalDestination,
    val nextStop: NextStop,
    val polyline: List<LatLng>
)