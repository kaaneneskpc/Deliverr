package com.kaaneneskpc.deliverr.data.models.response.add_to_cart

data class CartResponse(
    val checkoutDetails: CheckoutDetails,
    val items: List<CartItem>
)