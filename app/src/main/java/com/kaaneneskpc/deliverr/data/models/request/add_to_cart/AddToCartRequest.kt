package com.kaaneneskpc.deliverr.data.models.request.add_to_cart

data class AddToCartRequest(
    val restaurantId: String,
    val menuItemId: String,
    val quantity: Int
)