package com.kaaneneskpc.deliverr.data.models.response.order

import com.kaaneneskpc.deliverr.data.models.response.address.Address
import com.kaaneneskpc.deliverr.data.models.response.restaurant.Restaurant

data class Order(
    val address: Address,
    val createdAt: String,
    val id: String,
    val items: List<OrderItem>,
    val paymentStatus: String,
    val restaurant: Restaurant,
    val restaurantId: String,
    val status: String,
    val stripePaymentIntentId: String,
    val totalAmount: Double,
    val updatedAt: String,
    val userId: String
)