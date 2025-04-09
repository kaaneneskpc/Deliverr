package com.kaaneneskpc.deliverr.data.models.response.order

data class OrderItem(
    val id: String,
    val menuItemId: String,
    val orderId: String,
    val quantity: Int
)