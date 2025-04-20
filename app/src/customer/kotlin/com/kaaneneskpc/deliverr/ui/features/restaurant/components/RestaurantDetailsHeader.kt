package com.kaaneneskpc.deliverr.ui.features.restaurant.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kaaneneskpc.deliverr.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantDetailsHeader(
    imageUrl: String,
    restaurantID: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackButton: () -> Unit,
    onFavoriteButton: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = imageUrl, contentDescription = null, modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .sharedElement(
                    state = rememberSharedContentState(key = "image/${restaurantID}"),
                    animatedVisibilityScope
                )
                .clip(
                    RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                ), contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = onBackButton,
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .align(Alignment.TopStart)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
        }
        IconButton(
            onClick = onFavoriteButton,
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .align(Alignment.TopEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_favorite), contentDescription = null
            )
        }
    }
}