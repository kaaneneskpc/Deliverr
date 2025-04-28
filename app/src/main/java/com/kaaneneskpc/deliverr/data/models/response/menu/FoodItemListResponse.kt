package com.kaaneneskpc.deliverr.data.models.response.menu

import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItem

data class FoodItemListResponse(
    val foodItems: List<FoodItem>
)