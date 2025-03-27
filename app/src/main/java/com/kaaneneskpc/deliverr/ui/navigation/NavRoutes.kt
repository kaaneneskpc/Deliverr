package com.kaaneneskpc.deliverr.ui.navigation

import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItem
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object SignUp

@Serializable
object AuthScreen

@Serializable
object Home

@Serializable
data class RestaurantDetails(
    val restaurantId: String,
    val restaurantName: String,
    val restaurantImageUrl: String,
)

@Serializable
data class FoodDetails(val foodItem: FoodItem)

@Serializable
object Cart