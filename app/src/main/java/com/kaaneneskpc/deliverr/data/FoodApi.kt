package com.kaaneneskpc.deliverr.data

import com.kaaneneskpc.deliverr.data.models.response.auth.AuthResponse
import com.kaaneneskpc.deliverr.data.models.request.auth.OAuthRequest
import com.kaaneneskpc.deliverr.data.models.request.auth.SignInRequest
import com.kaaneneskpc.deliverr.data.models.request.auth.SignUpRequest
import com.kaaneneskpc.deliverr.data.models.response.home.CategoriesResponse
import com.kaaneneskpc.deliverr.data.models.response.home.RestaurantsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FoodApi {
    @GET("/categories")
    suspend fun getCategories(): Response<CategoriesResponse>
    @GET("/restaurants")
    suspend fun getRestaurants(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<RestaurantsResponse>
    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>
    @POST("/auth/login")
    suspend fun signIn(@Body request: SignInRequest): Response<AuthResponse>
    @POST("/auth/oauth")
    suspend fun oAuth(@Body request: OAuthRequest): Response<AuthResponse>
}