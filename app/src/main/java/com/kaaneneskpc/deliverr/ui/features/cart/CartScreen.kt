package com.kaaneneskpc.deliverr.ui.features.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kaaneneskpc.deliverr.R
import com.kaaneneskpc.deliverr.common.BasicDialog
import com.kaaneneskpc.deliverr.data.models.response.address.Address
import com.kaaneneskpc.deliverr.ui.features.cart.components.AddressCard
import com.kaaneneskpc.deliverr.ui.features.cart.components.CartHeaderView
import com.kaaneneskpc.deliverr.ui.features.cart.components.CartItemView
import com.kaaneneskpc.deliverr.ui.features.cart.components.CheckoutDetailsView
import com.kaaneneskpc.deliverr.ui.navigation.AddressList
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val showErrorDialog = remember {
        mutableStateOf(
            false
        )
    }
    val address =
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<Address?>(
            "address",
            null
        )?.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = address?.value) {
        address?.value?.let {
            viewModel.onAddressSelected(it)
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest {
            when (it) {
                is CartViewModel.CartEvent.onItemRemoveError,
                is CartViewModel.CartEvent.onQuantityUpdateError,
                is CartViewModel.CartEvent.showErrorDialog -> {
                    showErrorDialog.value = true
                }
                is CartViewModel.CartEvent.onAddressClicked -> {
                    navController.navigate(AddressList)
                }

                else -> {

                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        CartHeaderView(onBack = { navController.popBackStack() })
        Spacer(modifier = Modifier.size(16.dp))
        when (uiState.value) {
            is CartViewModel.CartUiState.Loading -> {
                Spacer(modifier = Modifier.size(16.dp))
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.size(16.dp))
                    CircularProgressIndicator()
                }
            }

            is CartViewModel.CartUiState.Success -> {
                val data = (uiState.value as CartViewModel.CartUiState.Success).data
                if (data.items.isNotEmpty()) {
                    LazyColumn {
                        items(data.items) { it ->
                            CartItemView(cartItem = it, onIncrement = { cartItem, _ ->
                                viewModel.incrementQuantity(cartItem)
                            }, onDecrement = { cartItem, _ ->
                                viewModel.decrementQuantity(cartItem)
                            }, onRemove = {
                                viewModel.removeItem(it)
                            })
                        }
                        item {
                            CheckoutDetailsView(data.checkoutDetails)
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cart),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                        Text(
                            text = "No items in cart",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }


            is CartViewModel.CartUiState.Error -> {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val message = (uiState.value as CartViewModel.CartUiState.Error).message
                    Text(text = message, style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Retry")
                    }

                }
            }

            CartViewModel.CartUiState.Nothing -> {}
        }

        val selectedAddress = viewModel.selectedAddress.collectAsStateWithLifecycle()
        Spacer(modifier = Modifier.weight(1f))
        if (uiState.value is CartViewModel.CartUiState.Success) {
            AddressCard(selectedAddress.value) {
                viewModel.onAddressClicked()
            }
            Button(
                onClick = { viewModel.checkout() },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedAddress.value != null
            ) {
                Text(text = "Checkout")
            }
        }

    }

    if (showErrorDialog.value) {
        ModalBottomSheet(onDismissRequest = { showErrorDialog.value = false }) {
            BasicDialog(title = viewModel.errorTitle, description = viewModel.errorMessage) {
                showErrorDialog.value = false
            }
        }
    }

}