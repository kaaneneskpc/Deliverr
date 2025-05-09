package com.kaaneneskpc.deliverr.data

import com.kaaneneskpc.deliverr.data.models.request.ReverseGeoCodeRequest
import com.kaaneneskpc.deliverr.data.models.request.add_to_cart.AddToCartRequest
import com.kaaneneskpc.deliverr.data.models.request.add_to_cart.UpdateCartItemRequest
import com.kaaneneskpc.deliverr.data.models.response.auth.AuthResponse
import com.kaaneneskpc.deliverr.data.models.request.auth.OAuthRequest
import com.kaaneneskpc.deliverr.data.models.request.auth.SignInRequest
import com.kaaneneskpc.deliverr.data.models.request.auth.SignUpRequest
import com.kaaneneskpc.deliverr.data.models.request.notification.FCMRequest
import com.kaaneneskpc.deliverr.data.models.request.payment.ConfirmPaymentRequest
import com.kaaneneskpc.deliverr.data.models.request.payment.PaymentIntentRequest
import com.kaaneneskpc.deliverr.data.models.response.GenericMessageResponse
import com.kaaneneskpc.deliverr.data.models.response.add_to_cart.AddToCartResponse
import com.kaaneneskpc.deliverr.data.models.response.add_to_cart.CartResponse
import com.kaaneneskpc.deliverr.data.models.response.address.Address
import com.kaaneneskpc.deliverr.data.models.response.address.AddressListResponse
import com.kaaneneskpc.deliverr.data.models.response.deliveries.DeliveriesListResponse
import com.kaaneneskpc.deliverr.data.models.response.deliveries.RiderDeliveryOrderListResponse
import com.kaaneneskpc.deliverr.data.models.response.home.CategoriesResponse
import com.kaaneneskpc.deliverr.data.models.response.menu.FoodItemListResponse
import com.kaaneneskpc.deliverr.data.models.response.menu.ImageUploadResponse
import com.kaaneneskpc.deliverr.data.models.response.notification.NotificationListResponse
import com.kaaneneskpc.deliverr.data.models.response.order.Order
import com.kaaneneskpc.deliverr.data.models.response.order.OrderListResponse
import com.kaaneneskpc.deliverr.data.models.response.payment.ConfirmPaymentResponse
import com.kaaneneskpc.deliverr.data.models.response.payment.PaymentIntentResponse
import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItem
import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItemResponse
import com.kaaneneskpc.deliverr.data.models.response.restaurant.Restaurant
import com.kaaneneskpc.deliverr.data.models.response.restaurant.RestaurantsResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @GET("/addresses")
    suspend fun getUserAddress(): Response<AddressListResponse>

    @POST("/addresses/reverse-geocode")
    suspend fun reverseGeocode(@Body request: ReverseGeoCodeRequest): Response<Address>

    @POST("/addresses")
    suspend fun storeAddress(@Body address: Address): Response<GenericMessageResponse>

    @POST("/payments/create-intent")
    suspend fun getPaymentIntent(@Body request: PaymentIntentRequest): Response<PaymentIntentResponse>

    @POST("/payments/confirm/{paymentIntentId}")
    suspend fun verifyPurchase(
        @Body request: ConfirmPaymentRequest,
        @Path("paymentIntentId") paymentIntentId: String
    ): Response<ConfirmPaymentResponse>

    @GET("/orders")
    suspend fun getOrders(): Response<OrderListResponse>

    @GET("/orders/{orderId}")
    suspend fun getOrderDetails(@Path("orderId") orderId: String): Response<Order>

    @PUT("/notifications/fcm-token")
    suspend fun updateToken(@Body request: FCMRequest): Response<GenericMessageResponse>

    @POST("/notifications/{id}/read")
    suspend fun readNotification(@Path("id") id: String): Response<GenericMessageResponse>

    @GET("/notifications")
    suspend fun getNotifications(): Response<NotificationListResponse>

    @GET("/restaurant-owner/profile")
    suspend fun getRestaurantProfile(): Response<Restaurant>

    @GET("/restaurant-owner/orders")
    suspend fun getRestaurantOrders(@Query("status") status: String): Response<OrderListResponse>

    @PATCH("orders/{orderId}/status")
    suspend fun updateOrderStatus(
        @Path("orderId") orderId: String,
        @Body map: Map<String, String>
    ): Response<GenericMessageResponse>

    @GET("/restaurants/{id}/menu")
    suspend fun getRestaurantMenu(@Path("id") restaurantId: String): Response<FoodItemListResponse>

    @POST("/restaurants/{id}/menu")
    suspend fun addRestaurantMenu(
        @Path("id") restaurantId: String,
        @Body foodItem: FoodItem
    ): Response<GenericMessageResponse>

    @POST("/images/upload")
    @Multipart
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ImageUploadResponse>

    @GET("/rider/deliveries/available")
    suspend fun getAvailableDeliveries(): Response<DeliveriesListResponse>

    @POST("/rider/deliveries/{orderId}/reject")
    suspend fun rejectDelivery(@Path("orderId") orderId: String): Response<GenericMessageResponse>

    @POST("/rider/deliveries/{orderId}/accept")
    suspend fun acceptDelivery(@Path("orderId") orderId: String): Response<GenericMessageResponse>

    @GET("/rider/deliveries/active")
    suspend fun getActiveDeliveries(): Response<RiderDeliveryOrderListResponse>
}