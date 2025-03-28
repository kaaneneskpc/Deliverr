package com.kaaneneskpc.deliverr.ui.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.models.request.add_to_cart.UpdateCartItemRequest
import com.kaaneneskpc.deliverr.data.models.request.payment.ConfirmPaymentRequest
import com.kaaneneskpc.deliverr.data.models.request.payment.PaymentIntentRequest
import com.kaaneneskpc.deliverr.data.models.response.add_to_cart.CartItem
import com.kaaneneskpc.deliverr.data.models.response.add_to_cart.CartResponse
import com.kaaneneskpc.deliverr.data.models.response.address.Address
import com.kaaneneskpc.deliverr.data.models.response.payment.PaymentIntentResponse
import com.kaaneneskpc.deliverr.data.remote.ApiResponse
import com.kaaneneskpc.deliverr.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(val foodApi: FoodApi) : ViewModel() {

    var errorTitle: String = ""
    var errorMessage: String = ""

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CartEvent>()
    val event = _event.asSharedFlow()

    private var cartResponse: CartResponse? = null
    private var paymentIntent: PaymentIntentResponse? = null

    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount = _cartItemCount.asStateFlow()

    private val address = MutableStateFlow<Address?>(null)
    val selectedAddress = address.asStateFlow()

    init {
        getCart()
    }


    fun getCart() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            val res = safeApiCall { foodApi.getCart() }
            when (res) {
                is ApiResponse.Success -> {
                    cartResponse = res.data
                    _cartItemCount.value = res.data.items.size
                    _uiState.value = CartUiState.Success(res.data)
                }

                is ApiResponse.Error -> {
                    _uiState.value = CartUiState.Error(res.message)
                }

                else -> {
                    _uiState.value = CartUiState.Error("An error occurred")
                }
            }
        }
    }

    fun incrementQuantity(cartItem: CartItem) {
        if (cartItem.quantity == 5) {
            return
        }
        updateItemQuantity(cartItem, cartItem.quantity + 1)
    }

    fun decrementQuantity(cartItem: CartItem) {
        if (cartItem.quantity == 1) {
            return
        }
        updateItemQuantity(cartItem, cartItem.quantity - 1)
    }

    private fun updateItemQuantity(cartItem: CartItem, quantity: Int) {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            val res =
                safeApiCall { foodApi.updateCart(UpdateCartItemRequest(cartItem.id, quantity)) }
            when (res) {
                is ApiResponse.Success -> {
                    getCart()
                }

                else -> {
                    cartResponse?.let {
                        _uiState.value = CartUiState.Success(cartResponse!!)
                    }
                    errorTitle = "Cannot Update Quantity"
                    errorMessage = "An error occurred while updating the quantity of the item"
                    _event.emit(CartEvent.onQuantityUpdateError)
                }
            }
        }
    }

    fun removeItem(cartItem: CartItem) {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            val res =
                safeApiCall { foodApi.deleteCartItem(cartItem.id) }
            when (res) {
                is ApiResponse.Success -> {
                    getCart()
                }

                else -> {
                    cartResponse?.let {
                        _uiState.value = CartUiState.Success(cartResponse!!)
                    }
                    errorTitle = "Cannot Delete"
                    errorMessage = "An error occurred while deleting the item"
                    _event.emit(CartEvent.onItemRemoveError)
                }
            }
        }
    }

    fun checkout() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            val paymentDetails =
                safeApiCall { foodApi.getPaymentIntent(PaymentIntentRequest(address.value?.id.orEmpty())) }

            when (paymentDetails) {
                is ApiResponse.Success -> {
                    paymentIntent = paymentDetails.data
                    _event.emit(CartEvent.OnInitiatePayment(paymentDetails.data))
                    _uiState.value = CartUiState.Success(cartResponse!!)
                }

                else -> {
                    errorTitle = "Cannot Checkout"
                    errorMessage = "An error occurred while checking out"
                    _event.emit(CartEvent.showErrorDialog)
                    _uiState.value = CartUiState.Success(cartResponse!!)
                }
            }
        }
    }


    fun onAddressClicked() {
        viewModelScope.launch {
            _event.emit(CartEvent.onAddressClicked)
        }
    }

    fun onAddressSelected(it: Address) {
        address.value = it
    }

    fun onPaymentSuccess() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            val response =
                safeApiCall {
                    foodApi.verifyPurchase(
                        ConfirmPaymentRequest(
                            paymentIntent!!.paymentIntentId,
                            address.value!!.id!!
                        ), paymentIntent!!.paymentIntentId
                    )
                }
            when (response) {
                is ApiResponse.Success -> {
                    _event.emit(CartEvent.OrderSuccess(response.data.orderId))
                    _uiState.value = CartUiState.Success(cartResponse!!)
                    getCart()
                }

                else -> {
                    errorTitle = "Payment Failed"
                    errorMessage = "An error occurred while processing your payment"
                    _event.emit(CartEvent.showErrorDialog)
                    _uiState.value = CartUiState.Success(cartResponse!!)
                }
            }
        }
    }

    fun onPaymentFailed() {
        errorTitle = "Payment Failed"
        errorMessage = "An error occurred while processing your payment"
        viewModelScope.launch {
            _event.emit(CartEvent.showErrorDialog)
        }
    }

    sealed class CartUiState {
        object Nothing : CartUiState()
        object Loading : CartUiState()
        data class Success(val data: CartResponse) : CartUiState()
        data class Error(val message: String) : CartUiState()
    }

    sealed class CartEvent {
        object showErrorDialog : CartEvent()
        data class OrderSuccess(val orderId: String?) : CartEvent()
        object OnCheckout : CartEvent()
        data class OnInitiatePayment(val data: PaymentIntentResponse) : CartEvent()
        object onQuantityUpdateError : CartEvent()
        object onItemRemoveError : CartEvent()
        object onAddressClicked : CartEvent()
    }
}