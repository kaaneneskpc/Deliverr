package com.kaaneneskpc.deliverr.ui.feature.order_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaaneneskpc.deliverr.util.OrdersUtils

@Composable
fun StatusChip(status: String) {
    val (backgroundColor, textColor) = when (status) {
        OrdersUtils.OrderStatus.PENDING_ACCEPTANCE.name -> Pair(Color(0xFFFFF9C4), Color(0xFFAF9500))
        OrdersUtils.OrderStatus.ACCEPTED.name -> Pair(Color(0xFFE0F7FA), Color(0xFF00838F))
        OrdersUtils.OrderStatus.PREPARING.name -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        OrdersUtils.OrderStatus.READY.name -> Pair(Color(0xFFE3F2FD), Color(0xFF1565C0))
        OrdersUtils.OrderStatus.OUT_FOR_DELIVERY.name -> Pair(Color(0xFFF3E5F5), Color(0xFF6A1B9A))
        OrdersUtils.OrderStatus.DELIVERED.name -> Pair(Color(0xFFE8F5E9), Color(0xFF1B5E20))
        OrdersUtils.OrderStatus.REJECTED.name -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
        OrdersUtils.OrderStatus.CANCELLED.name -> Pair(Color(0xFFEFEBE9), Color(0xFF4E342E))
        else -> Pair(Color(0xFFEEEEEE), Color(0xFF424242))
    }
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = status.replace("_", " ").lowercase()
                .split(" ")
                .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } },
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
} 