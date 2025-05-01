package com.kaaneneskpc.deliverr.ui.features.orders.orderMap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.kaaneneskpc.deliverr.R
import com.kaaneneskpc.deliverr.data.models.response.order.Order
import com.kaaneneskpc.deliverr.ui.features.orders.OrderDetailsBaseViewModel

@Composable
fun OrderTrackerMapView(modifier: Modifier, viewModel: OrderDetailsBaseViewModel, order: Order) {
    val context = LocalContext.current
    val messages = viewModel.locationUpdate.collectAsStateWithLifecycle(null)
    Column(modifier = Modifier.fillMaxSize()) {
        val cameraPositionState = rememberCameraPositionState()
        LaunchedEffect(key1 = messages.value != null) {
            messages.value?.let {
                val riderMarker = LatLng(it.currentLocation.latitude, it.currentLocation.longitude)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(riderMarker, 15f)
            }
        }
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            cameraPositionState = cameraPositionState
        ) {
            messages.value?.let {
                val riderMarker = LatLng(it.currentLocation.latitude, it.currentLocation.longitude)
                val state = rememberMarkerState(position = riderMarker)
                LaunchedEffect(key1 = riderMarker) {
                    state.position = riderMarker
                }
                val destinationMarker =
                    LatLng(it.finalDestination.latitude, it.finalDestination.longitude)
                Marker(
                    state = state,
                    title = "Rider",
                    snippet = "Rider",
                    icon = bitmapDescriptorFromVector(
                        context = context,
                        vectorResId = R.drawable.ic_delivery
                    )
                )

                Marker(
                    state = rememberMarkerState(position = destinationMarker),
                    title = "Customer",
                    snippet = "Customer",
                    icon = bitmapDescriptorFromVector(
                        context = context,
                        vectorResId = R.drawable.home
                    )
                )
                Polyline(
                    points = it.polyline,
                    color = MaterialTheme.colorScheme.primary,
                    width = 8f,
                )
            }
        }
    }
}


fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorResId: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable!!.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}