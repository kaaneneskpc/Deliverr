package com.kaaneneskpc.deliverr.common

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun RequestLocationPermission(onPermissionGranted: () -> Unit, onPermissionRejected: () -> Unit) {
    val context = LocalContext.current
    if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    ) {
        onPermissionGranted()
        return
    }
    val permission = listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (result.values.all { it }) {
                onPermissionGranted()
            } else {
                onPermissionRejected()
            }
        }
    LaunchedEffect(key1 = Unit) {
        permissionLauncher.launch(permission.toTypedArray())
    }
}