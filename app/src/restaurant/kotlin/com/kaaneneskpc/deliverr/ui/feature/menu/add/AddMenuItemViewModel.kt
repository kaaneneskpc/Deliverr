package com.kaaneneskpc.deliverr.ui.feature.menu.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.deliverr.DeliverrSession
import com.kaaneneskpc.deliverr.data.FoodApi
import com.kaaneneskpc.deliverr.data.models.response.restaurant.FoodItem

import com.kaaneneskpc.deliverr.data.remote.ApiResponse
import com.kaaneneskpc.deliverr.data.remote.safeApiCall
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class AddMenuItemViewModel @Inject constructor(
    val foodApi: FoodApi,
    val session: DeliverrSession,
    @ApplicationContext val context: Context
) :
    ViewModel() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _price = MutableStateFlow("")
    val price = _price.asStateFlow()

    private val _imageUrl = MutableStateFlow<Uri?>(null)
    val imageUrl = _imageUrl.asStateFlow()


    private val _addMenuItemState = MutableStateFlow<AddMenuItemState>(AddMenuItemState.Idle)
    val addMenuItemState = _addMenuItemState.asStateFlow()

    private val _addMenuItemEvent = MutableSharedFlow<AddMenuItemEvent>()
    val addMenuItemEvent = _addMenuItemEvent.asSharedFlow()


    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onPriceChange(price: String) {
        _price.value = price
    }

    fun onImageUrlChange(imageUrl: Uri?) {
        _imageUrl.value = imageUrl
    }

    fun addMenuItem() {
        val name = name.value
        val description = description.value
        val price = price.value.toDoubleOrNull() ?: 0.0
        val restaurantId = session.getRestaurantId() ?: ""

        if (name.isEmpty() || description.isEmpty() || price == 0.0) {
            _addMenuItemEvent.tryEmit(AddMenuItemEvent.ShowErrorMessage("Please fill all fields"))
            return
        }
        viewModelScope.launch {
            _addMenuItemState.value = AddMenuItemState.Loading
            val imageUrl = imageUrl.value?.let { uploadImage(imageUri = it) }
            val response = safeApiCall {
                foodApi.addRestaurantMenu(
                    restaurantId,
                    FoodItem(
                        name = name,
                        description = description,
                        price = price,
                        imageUrl = imageUrl.orEmpty(),
                        restaurantId = restaurantId
                    )
                )
            }
            when (response) {
                is ApiResponse.Success -> {
                    _addMenuItemState.value = AddMenuItemState.Success("Item added successfully")
                    _addMenuItemEvent.emit(AddMenuItemEvent.GoBack)
                }

                is ApiResponse.Error -> {
                    _addMenuItemState.value = AddMenuItemState.Error(response.message)
                }

                is ApiResponse.Exception -> {
                    _addMenuItemState.value = AddMenuItemState.Error("Network Error")
                }
            }
        }
    }

    suspend fun uploadImage(imageUri: Uri): String? {
        val file = fileFromUri(imageUri)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
        val response = safeApiCall { foodApi.uploadImage(multipartBody) }
        when (response) {
            is ApiResponse.Success -> {
                return response.data.url
            }

            else -> {
                return null
            }
        }
    }

    private fun fileFromUri(imageUri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val file = File.createTempFile(
            "temp-${System.currentTimeMillis()}-foodhub",
            "jpg",
            context.cacheDir
        )
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    fun onImageClicked() {
        viewModelScope.launch {
            _addMenuItemEvent.emit(AddMenuItemEvent.AddNewImage)
        }
    }


    sealed class AddMenuItemState {
        object Idle : AddMenuItemState()
        object Loading : AddMenuItemState()
        data class Success(val message: String) : AddMenuItemState()
        data class Error(val message: String) : AddMenuItemState()
    }

    sealed class AddMenuItemEvent {
        data class ShowErrorMessage(val message: String) : AddMenuItemEvent()
        object AddNewImage : AddMenuItemEvent()
        object GoBack : AddMenuItemEvent()
    }

}