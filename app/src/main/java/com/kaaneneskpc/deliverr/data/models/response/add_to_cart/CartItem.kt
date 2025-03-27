package com.kaaneneskpc.deliverr.data.models.response.add_to_cart

import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItem

data class CartItem(
    val addedAt: String,
    val id: String,
    val menuItemId: FoodItem,
    val quantity: Int,
    val restaurantId: String,
    val userId: String
)