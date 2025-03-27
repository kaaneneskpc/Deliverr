package com.kaaneneskpc.deliverr.data.models.request.add_to_cart

data class UpdateCartItemRequest(
    val cartItemId: String,
    val quantity: Int
)