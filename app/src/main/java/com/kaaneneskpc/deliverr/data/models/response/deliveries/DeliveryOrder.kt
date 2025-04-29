package com.kaaneneskpc.deliverr.data.models.response.deliveries

import com.kaaneneskpc.deliverr.data.models.response.restaurant.Restaurant

data class DeliveryOrder(
    val createdAt: String,
    val customer: Customer,
    val estimatedEarning: Double,
    val items: List<DeliveryOrderItem>,
    val orderId: String,
    val restaurant: Restaurant,
    val status: String,
    val totalAmount: Double,
    val updatedAt: String
)