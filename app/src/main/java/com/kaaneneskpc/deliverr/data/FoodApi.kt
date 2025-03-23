package com.kaaneneskpc.deliverr.data

import com.kaaneneskpc.deliverr.data.models.AuthResponse
import com.kaaneneskpc.deliverr.data.models.OAuthRequest
import com.kaaneneskpc.deliverr.data.models.SignInRequest
import com.kaaneneskpc.deliverr.data.models.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodApi {
    @GET("/food")
    suspend fun getFood(): List<String>

    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): AuthResponse
    @POST("/auth/login")
    suspend fun signIn(@Body request: SignInRequest): AuthResponse
    @POST("/auth/oauth")
    suspend fun oAuth(@Body request: OAuthRequest): AuthResponse
}