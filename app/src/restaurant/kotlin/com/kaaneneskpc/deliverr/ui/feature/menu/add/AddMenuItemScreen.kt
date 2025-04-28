package com.kaaneneskpc.deliverr.ui.feature.menu.add

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kaaneneskpc.deliverr.ui.navigation.ImagePicker
import com.kaaneneskpc.deliverr.ui.theme.Primary
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
                title = { 
                    Text(
                        "Add Menu Item", 
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleMedium
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clickable { viewModel.onImageClicked() },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 2.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImage.value != null) {
                        AsyncImage(
                            model = selectedImage.value,
                            contentDescription = "Food Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(percent = 50),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.size(72.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = "Add Photo",
                                        modifier = Modifier.size(32.dp),
                                        tint = Primary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Add food image",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap to select from gallery",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
            
            OutlinedTextField(
                value = name.value,
                onValueChange = { viewModel.onNameChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Menu Item Name") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )
            
            OutlinedTextField(
                value = description.value,
                onValueChange = { viewModel.onDescriptionChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Description") },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )
            
            OutlinedTextField(
                value = price.value,
                onValueChange = { viewModel.onPriceChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Price") },
                singleLine = true,
                prefix = { Text("₺", style = MaterialTheme.typography.bodyMedium) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )
            
            if (uiState.value is AddMenuItemViewModel.AddMenuItemState.Error) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = (uiState.value as AddMenuItemViewModel.AddMenuItemState.Error).message,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            
            // Button
            ElevatedButton(
                onClick = { viewModel.addMenuItem() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                enabled = uiState.value !is AddMenuItemViewModel.AddMenuItemState.Loading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 2.dp
                )
            ) {
                if (uiState.value is AddMenuItemViewModel.AddMenuItemState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Adding Item...",
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        "Add Menu Item",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}