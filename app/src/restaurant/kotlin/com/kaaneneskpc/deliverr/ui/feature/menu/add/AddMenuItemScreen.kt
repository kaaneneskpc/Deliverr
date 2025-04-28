package com.kaaneneskpc.deliverr.ui.feature.menu.add

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kaaneneskpc.deliverr.common.DeliverrTextField
import com.kaaneneskpc.deliverr.ui.navigation.ImagePicker
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddMenuItemScreen(
    navController: NavController,
    viewModel: AddMenuItemViewModel = hiltViewModel()
) {

    val name = viewModel.name.collectAsStateWithLifecycle()
    val description = viewModel.description.collectAsStateWithLifecycle()
    val price = viewModel.price.collectAsStateWithLifecycle()
    val uiState = viewModel.addMenuItemState.collectAsStateWithLifecycle()
    val selectedImage = viewModel.imageUrl.collectAsStateWithLifecycle()

    val imageUri =
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<Uri?>("imageUri", null)
            ?.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = imageUri?.value) {
        imageUri?.value?.let {
            viewModel.onImageUrlChange(it)
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.addMenuItemEvent.collectLatest {
            when (it) {
                is AddMenuItemViewModel.AddMenuItemEvent.GoBack -> {
                    Toast.makeText(
                        navController.context, "Item added Successfully", Toast.LENGTH_SHORT
                    ).show()
                    navController.previousBackStackEntry?.savedStateHandle?.set("added", true)
                    navController.popBackStack()

                }

                is AddMenuItemViewModel.AddMenuItemEvent.AddNewImage -> {
                    navController.navigate(ImagePicker)
                }

                is AddMenuItemViewModel.AddMenuItemEvent.ShowErrorMessage -> {
                    Toast.makeText(navController.context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Add Menu Item")
        AsyncImage(
            model = selectedImage.value,
            contentDescription = "Food Image",
            modifier = Modifier
                .size(140.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(LightGray)
                .clickable {
                    viewModel.onImageClicked()
                }
        )
        DeliverrTextField(value = name.value, onValueChange = {
            viewModel.onNameChange(it)
        }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Name") })
        DeliverrTextField(value = description.value, onValueChange = {
            viewModel.onDescriptionChange(it)
        },
            modifier = Modifier.fillMaxWidth(), label = { Text(text = "Description") })
        DeliverrTextField(value = price.value, onValueChange = {
            viewModel.onPriceChange(it)
        }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Price") })
        if (uiState.value is AddMenuItemViewModel.AddMenuItemState.Loading) {
            Button(onClick = { }, enabled = false) {
                Text(text = "Adding Item...")
            }
        } else {
            if (uiState.value is AddMenuItemViewModel.AddMenuItemState.Error) {
                Text(
                    text = (uiState.value as AddMenuItemViewModel.AddMenuItemState.Error).message,
                    color = Red
                )
            }
            Button(onClick = { viewModel.addMenuItem() }) {
                Text(text = "Add Menu Item")
            }
        }
    }
}