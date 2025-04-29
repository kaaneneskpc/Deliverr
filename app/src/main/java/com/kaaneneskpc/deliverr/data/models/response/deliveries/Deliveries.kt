package com.kaaneneskpc.deliverr.data.models.response.deliveries

data class Deliveries(
    val createdAt: String,
    val customerAddress: String,
    val estimatedDistance: Double,
    val estimatedEarning: Double,
    val orderAmount: Double,
    val orderId: String,
    val restaurantAddress: String,
    val restaurantName: String
)