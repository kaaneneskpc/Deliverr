package com.kaaneneskpc.deliverr.data.models.response.deliveries

data class DeliveryOrderItem(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int
)