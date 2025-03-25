package com.kaaneneskpc.deliverr.data.models.response.home

data class Restaurant(
    val address: String,
    val categoryId: String,
    val createdAt: String,
    val distance: Double,
    val id: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val ownerId: String
)