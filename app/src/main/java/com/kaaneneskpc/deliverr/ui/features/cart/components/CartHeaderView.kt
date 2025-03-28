package com.kaaneneskpc.deliverr.ui.features.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kaaneneskpc.deliverr.R

@Composable
fun CartHeaderView(onBack: () -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = SpaceBetween,
    ) {
        IconButton(onClick = onBack) {
            Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
        }
        Text(text = "Cart", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 12.dp, end = 40.dp))
        Spacer(modifier = Modifier.size(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun CartHeaderViewPreview() {
    CartHeaderView(onBack = {})
}