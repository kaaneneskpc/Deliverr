package com.kaaneneskpc.deliverr.data

import com.kaaneneskpc.deliverr.data.models.request.add_to_cart.AddToCartRequest
import com.kaaneneskpc.deliverr.data.models.request.add_to_cart.UpdateCartItemRequest
import com.kaaneneskpc.deliverr.data.models.response.auth.AuthResponse
import com.kaaneneskpc.deliverr.data.models.request.auth.OAuthRequest
import com.kaaneneskpc.deliverr.data.models.request.auth.SignInRequest
import com.kaaneneskpc.deliverr.data.models.request.auth.SignUpRequest
import com.kaaneneskpc.deliverr.data.models.response.GenericMessageResponse
import com.kaaneneskpc.deliverr.data.models.response.add_to_cart.AddToCartResponse
import com.kaaneneskpc.deliverr.data.models.response.add_to_cart.CartResponse
import com.kaaneneskpc.deliverr.data.models.response.home.CategoriesResponse
import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItemResponse
import com.kaaneneskpc.deliverr.data.models.response.restaurant.RestaurantsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {
    @GET("/categories")
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET("/restaurants")
    suspend fun getRestaurants(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<RestaurantsResponse>

    @GET("/restaurants/{restaurantId}/menu")
    suspend fun getFoodItemForRestaurant(@Path("restaurantId") restaurantId: String): Response<FoodItemResponse>

    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>

    @POST("/auth/login")
    suspend fun signIn(@Body request: SignInRequest): Response<AuthResponse>

    @POST("/auth/oauth")
    suspend fun oAuth(@Body request: OAuthRequest): Response<AuthResponse>

    @POST("/cart")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<AddToCartResponse>

    @GET("/cart")
    suspend fun getCart(): Response<CartResponse>

    @PATCH("/cart")
    suspend fun updateCart(@Body request: UpdateCartItemRequest): Response<GenericMessageResponse>

    @DELETE("/cart/{cartItemId}")
    suspend fun deleteCartItem(@Path("cartItemId") cartItemId: String): Response<GenericMessageResponse>
}