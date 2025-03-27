package com.kaaneneskpc.deliverr.data.models.response.add_to_cart

data class CheckoutDetails(
    val deliveryFee: Double,
    val subTotal: Double,
    val tax: Double,
    val totalAmount: Double
)