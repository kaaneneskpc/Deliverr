package com.kaaneneskpc.deliverr.ui.features.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kaaneneskpc.deliverr.data.models.response.address.Address

@Composable
fun AddressCard(address: Address?, onAddressClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp)
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(Color.White)
            .clickable { onAddressClicked.invoke() }
            .padding(16.dp)

    ) {
        if (address != null) {
            Column {
                Text(text = address.addressLine1, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "${address.city}, ${address.state}, ${address.country}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        } else {
            Text(text = "Select Address", style = MaterialTheme.typography.bodyMedium)
        }
    }

}