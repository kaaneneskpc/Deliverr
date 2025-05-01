package com.kaaneneskpc.deliverr.ui.features.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItem
import com.kaaneneskpc.deliverr.ui.theme.Primary
import com.kaaneneskpc.deliverr.ui.theme.Typography

@SuppressLint("DefaultLocale")
@Composable
fun PopularFoodItem(foodItem: FoodItem, onFoodItemSelected: (FoodItem) -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp)
            .height(200.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onFoodItemSelected(foodItem) }
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = foodItem.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Primary,
                        spotColor = Primary
                    ),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = foodItem.name,
                style = Typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${String.format("%.2f", foodItem.price)}",
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "4.8",
                        style = Typography.bodySmall,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPopularFoodItem() {
    PopularFoodItem(
        foodItem = FoodItem(
            id = "1",
            name = "Beef Burger",
            price = 12.99,
            description = "Delicious beef burger with cheese and veggies",
            imageUrl = "",
            restaurantId = "123",
            createdAt = ""
        ),
        onFoodItemSelected = {}
    )
} 