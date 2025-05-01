package com.kaaneneskpc.deliverr.data.models.response.socket

import kotlinx.serialization.Serializable

@Serializable
data class FinalDestination(
    val address: String,
    val latitude: Double,
    val longitude: Double
)