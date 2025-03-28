package com.kaaneneskpc.deliverr.data.models.response.address

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val id: String? = null,
    val userId: String? = null,
    val addressLine1: String,
    val addressLine2: String? = null,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val latitude: Double? = null,
    val longitude: Double? = null
) : Parcelable